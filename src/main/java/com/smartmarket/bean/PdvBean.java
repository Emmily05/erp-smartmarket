package com.smartmarket.bean;

import com.smartmarket.model.ItemVenda;
import com.smartmarket.model.Produto;
import com.smartmarket.model.Venda;
import com.smartmarket.model.FormaPagamento;
import com.smartmarket.service.ProdutoService;
import com.smartmarket.service.VendaService;
import com.smartmarket.util.CupomFiscalPDFGenerator;
import com.smartmarket.util.FacesUtil;
import com.smartmarket.util.SessionUtil;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Named
@SessionScoped
public class PdvBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private ProdutoService produtoService = new ProdutoService();
    private VendaService vendaService = new VendaService();

    private Venda vendaAtual;
    private String codigoProduto;
    private BigDecimal valorPago;
    private FormaPagamento formaPagamentoSelecionada;
    private Venda ultimaVenda;

    @PostConstruct
    public void init() {
        iniciarNovaVenda();
    }

    public void iniciarNovaVenda() {
        vendaAtual = new Venda();
        vendaAtual.setUsuarioId(SessionUtil.getUsuarioLogado().getId());
        vendaAtual.setUsuario(SessionUtil.getUsuarioLogado());
        codigoProduto = null;
        valorPago = BigDecimal.ZERO;
        formaPagamentoSelecionada = FormaPagamento.DINHEIRO; 
        ultimaVenda = null;
    }

    public void adicionarProduto() {
        if (codigoProduto == null || codigoProduto.trim().isEmpty()) {
            FacesUtil.addErrorMessage("Erro", "Informe o código do produto.");
            return;
        }

        List<Produto> produtos = produtoService.findAll();
        Produto produtoEncontrado = produtos.stream()
                .filter(p -> p.getCodigo() != null && p.getCodigo().equals(codigoProduto.trim()))
                .findFirst()
                .orElse(null);

        if (produtoEncontrado == null) {
            FacesUtil.addErrorMessage("Erro", "Produto com código " + codigoProduto + " não encontrado.");
            codigoProduto = null;
            return;
        }
        
        if (produtoEncontrado.getEstoque().compareTo(BigDecimal.ONE) < 0) {
            FacesUtil.addErrorMessage("Erro", "Estoque insuficiente para o produto: " + produtoEncontrado.getNome());
            codigoProduto = null;
            return;
        }

        ItemVenda itemExistente = vendaAtual.getItens().stream()
                .filter(item -> item.getProdutoId().equals(produtoEncontrado.getId()))
                .findFirst()
                .orElse(null);

        if (itemExistente != null) {
            itemExistente.setQuantidade(itemExistente.getQuantidade().add(BigDecimal.ONE));
        } else {
            ItemVenda novoItem = new ItemVenda();
            novoItem.setProduto(produtoEncontrado);
            novoItem.setQuantidade(BigDecimal.ONE);
            vendaAtual.getItens().add(novoItem);
        }

        calcularTotal();
        codigoProduto = null;
        FacesUtil.addInfoMessage("Produto Adicionado", produtoEncontrado.getNome());
    }

    public void removerItem(ItemVenda item) {
        vendaAtual.getItens().remove(item);
        calcularTotal();
        FacesUtil.addInfoMessage("Item Removido", item.getProduto().getNome());
    }

    private void calcularTotal() {
        BigDecimal total = vendaAtual.getItens().stream()
                .map(ItemVenda::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        vendaAtual.setTotal(total.setScale(2, RoundingMode.HALF_UP));
    }

    public void finalizarVenda() {
        if (vendaAtual.getItens().isEmpty()) {
            FacesUtil.addErrorMessage("Erro", "A venda não pode ser finalizada sem itens.");
            return;
        }
        
	        if (formaPagamentoSelecionada == null) {
	            FacesUtil.addErrorMessage("Erro", "Selecione a forma de pagamento.");
	            return;
	        }
	        
	        vendaAtual.setFormaPagamento(formaPagamentoSelecionada);

	        if (formaPagamentoSelecionada == FormaPagamento.DINHEIRO && valorPago.compareTo(vendaAtual.getTotal()) < 0) {
	            FacesUtil.addErrorMessage("Erro", "Valor pago insuficiente para pagamento em Dinheiro.");
	            return;
	        }
        try {
	            // 1. Calcula troco e seta forma de pagamento
        	vendaAtual.setDataHora(LocalDateTime.now());
        	vendaAtual.setUsuarioNome(SessionUtil.getUsuarioLogado().getNome());
        	vendaAtual.setValorPago(valorPago);
        	vendaAtual.setTroco(formaPagamentoSelecionada == FormaPagamento.DINHEIRO 
        	    ? valorPago.subtract(vendaAtual.getTotal()) 
        	    : BigDecimal.ZERO);

            // 2. Salva a venda e os itens (transação)
	            vendaAtual.setDataHora(LocalDateTime.now());
	            vendaAtual.setUsuarioNome(SessionUtil.getUsuarioLogado().getNome());
	            vendaAtual.setUsuario(SessionUtil.getUsuarioLogado());

	            vendaService.save(vendaAtual);
            
            // 3. Dá baixa no estoque
            for (ItemVenda item : vendaAtual.getItens()) {
                produtoService.darBaixaEstoque(item.getProduto(), item.getQuantidade().intValue());
            }
            
            // 4. Armazena a última venda para o cupom fiscal
            ultimaVenda = vendaAtual;

            FacesUtil.addInfoMessage("Venda Finalizada", "Venda Nº " + vendaAtual.getId() + " concluída com sucesso!");
            
            // Inicia uma nova venda após a finalização
            iniciarNovaVenda();

        } catch (Exception e) {
            FacesUtil.addErrorMessage("Erro", "Erro ao finalizar a venda: " + e.getMessage());
        }
    }
    
	    public void gerarCupomFiscal() throws IOException {
	        if (ultimaVenda == null) {
	            FacesUtil.addErrorMessage("Erro", "Nenhuma venda recente para gerar cupom.");
	            return;
	        }
	        
	        FacesContext facesContext = FacesContext.getCurrentInstance();
	        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
	        
	        try {
	            byte[] pdfBytes = CupomFiscalPDFGenerator.generate(ultimaVenda);
	            
	            response.setContentType("application/pdf");
	            response.setHeader("Content-disposition", "attachment; filename=cupom_fiscal_" + ultimaVenda.getId() + ".pdf");
	            response.setContentLength(pdfBytes.length);
	            
	            OutputStream output = response.getOutputStream();
	            output.write(pdfBytes);
	            output.flush();
	            
	        } catch (Exception e) {
	            FacesUtil.addErrorMessage("Erro", "Erro ao gerar o cupom fiscal em PDF: " + e.getMessage());
	        } finally {
	            facesContext.responseComplete();
	        }
	    }


    public Venda getVendaAtual() {
        return vendaAtual;
    }

    public String getCodigoProduto() {
        return codigoProduto;
    }

    public void setCodigoProduto(String codigoProduto) {
        this.codigoProduto = codigoProduto;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }

	    public Venda getUltimaVenda() {
	        return ultimaVenda;
	    }

	    public FormaPagamento[] getFormasPagamento() {
	        return FormaPagamento.values();
	    }

	    public FormaPagamento getFormaPagamentoSelecionada() {
	        return formaPagamentoSelecionada;
	    }

	    public void setFormaPagamentoSelecionada(FormaPagamento formaPagamentoSelecionada) {
	        this.formaPagamentoSelecionada = formaPagamentoSelecionada;
	    }
	}
