package bean;

import jakarta.annotation.PostConstruct;
import jakarta.faces.bean.ManagedBean;
import jakarta.faces.bean.ViewScoped;
import modelo.PessoaSalarioConsolidado;
import service.PessoaService;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@ManagedBean
@ViewScoped
public class PessoaBean implements Serializable {

    private List<PessoaSalarioConsolidado> pessoas;
    private PessoaService service = new PessoaService();

    private Integer novaPessoaId;
    private String novaPessoaNome;
    private Integer novaPessoaCargoId;
    private String novaPessoaCidade;
    private String novaPessoaEmail;
    private String novaPessoaCep;
    private String novaPessoaEndereco;
    private String novaPessoaPais;
    private String novaPessoaUsuario;
    private String novaPessoaTelefone;
    private Date novaPessoaDataNascimento;

    private int paginaAtual = 0;
    private int tamanhoPagina = 10;

    @PostConstruct
    public void init() {
        carregarPessoas();
    }

    public void carregarPessoas() {
        pessoas = service.listarSalarios();
    }

    public void proximaPagina() {
        if (pessoas != null && (paginaAtual + 1) * tamanhoPagina < pessoas.size()) {
            paginaAtual++;
        }
    }

    public void paginaAnterior() {
        if (paginaAtual > 0) {
            paginaAtual--;
        }
    }

    public List<PessoaSalarioConsolidado> getPessoasPaginadas() {
        if (pessoas == null || pessoas.isEmpty()) {
            return Collections.emptyList();
        }
        int start = paginaAtual * tamanhoPagina;
        int end = Math.min(start + tamanhoPagina, pessoas.size());
        return pessoas.subList(start, end);
    }

    public void recalcular() {
        service.recalcularSalarios();
        carregarPessoas();
    }

	public void adicionarPessoa() {
		service.adicionarPessoa(novaPessoaNome, novaPessoaCargoId, novaPessoaCidade, novaPessoaEmail, novaPessoaCep,
				novaPessoaEndereco, novaPessoaPais, novaPessoaUsuario, novaPessoaTelefone, novaPessoaDataNascimento);
		limparFormulario();
		carregarPessoas();
	}
    
    private void limparFormulario() {
        novaPessoaNome = null;
        novaPessoaCargoId = null;
        novaPessoaCidade = null;
        novaPessoaEmail = null;
        novaPessoaCep = null;
        novaPessoaEndereco = null;
        novaPessoaPais = null;
        novaPessoaUsuario = null;
        novaPessoaTelefone = null;
        novaPessoaDataNascimento = null;
    }

    public void atualizarPessoa() {
        service.atualizarPessoa(novaPessoaId, novaPessoaNome, novaPessoaCargoId);
        carregarPessoas();
    }

    public void excluirPessoa(int id) {
        service.excluirPessoa(id);
        carregarPessoas();
    }

    public List<PessoaSalarioConsolidado> getPessoas() {
        if (pessoas == null)
            carregarPessoas();
        return pessoas;
    }

    public int getPaginaAtual() {
        return paginaAtual;
    }

    public int getTamanhoPagina() {
        return tamanhoPagina;
    }

    public String getNovaPessoaNome() {
        return novaPessoaNome;
    }

    public void setNovaPessoaNome(String novaPessoaNome) {
        this.novaPessoaNome = novaPessoaNome;
    }

    public Integer getNovaPessoaCargoId() { 
        return novaPessoaCargoId; 
    }
    public void setNovaPessoaCargoId(Integer novaPessoaCargoId) { 
        this.novaPessoaCargoId = novaPessoaCargoId; 
    }
    
	public String getNovaPessoaCidade() {
		return novaPessoaCidade;
	}

	public void setNovaPessoaCidade(String novaPessoaCidade) {
		this.novaPessoaCidade = novaPessoaCidade;
	}

	public String getNovaPessoaEmail() {
		return novaPessoaEmail;
	}

	public void setNovaPessoaEmail(String novaPessoaEmail) {
		this.novaPessoaEmail = novaPessoaEmail;
	}

	public String getNovaPessoaCep() {
		return novaPessoaCep;
	}

	public void setNovaPessoaCep(String novaPessoaCep) {
		this.novaPessoaCep = novaPessoaCep;
	}

	public String getNovaPessoaEndereco() {
		return novaPessoaEndereco;
	}

	public void setNovaPessoaEndereco(String novaPessoaEndereco) {
		this.novaPessoaEndereco = novaPessoaEndereco;
	}

	public String getNovaPessoaPais() {
		return novaPessoaPais;
	}

	public void setNovaPessoaPais(String novaPessoaPais) {
		this.novaPessoaPais = novaPessoaPais;
	}

	public String getNovaPessoaUsuario() {
		return novaPessoaUsuario;
	}

	public void setNovaPessoaUsuario(String novaPessoaUsuario) {
		this.novaPessoaUsuario = novaPessoaUsuario;
	}

	public String getNovaPessoaTelefone() {
		return novaPessoaTelefone;
	}

	public void setNovaPessoaTelefone(String novaPessoaTelefone) {
		this.novaPessoaTelefone = novaPessoaTelefone;
	}

	public Date getNovaPessoaDataNascimento() {
		return novaPessoaDataNascimento;
	}

	public void setNovaPessoaDataNascimento(Date novaPessoaDataNascimento) {
		this.novaPessoaDataNascimento = novaPessoaDataNascimento;
	}

    public int getNovaPessoaId() {
        return novaPessoaId;
    }

    public void setNovaPessoaId(int novaPessoaId) {
        this.novaPessoaId = novaPessoaId;
    }
}