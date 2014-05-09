var Sala;
var UsuarioLogado;
var PautaRealizada;

function logar() {
    var usuario = document.getElementById("txtUsuario").value;
    var senha = document.getElementById("txtSenha").value;

    PautaRealizada = false;

    if (usuario == "") {
        alert("Favor informar o usuario.");
        document.getElementById("txtUsuario").focus();
        return false;
    }
    else if (senha == "") {
        alert("Favor informar senha.");
        document.getElementById("txtSenha").focus();
        return false;
    }

    var funcao = 5; //Desconhecido
    var validado = false;

    for (i = 0; i < Sala.usuarios.length; i++) {
        if (Sala.usuarios[i].idAcessoRede == usuario &&
            Sala.usuarios[i].password == senha) {
            funcao = Sala.usuarios[i].funcao;
            validado = true;
            UsuarioLogado = Sala.usuarios[i];
            break;
        }
    }

    if (validado) {
        if (usuarioTemPermissao(funcao)) {
            redirecionar("index.html");
        }
    }
    else {
        alert("Usuario ou senha incorreto.");
        document.getElementById("txtUsuario").focus();
        carregarSala();
        return false;
    }
}

function usuarioTemPermissao(funcao) {
    var result = false;
    switch (funcao) {
        case 0: //ADMINISTRADOR
        case 1: //PROFESSOR
        case 3: //SUPORTE
            result = true;
            break;
        case 2: //ALUNO
        case 4: //CONVIDADO
        case 5: //DESCONHECIDO
        default:
            result = false;
            break;
    }

    return result;
}

function carregarSala() {
    $.support.cors = true;
    $.ajax({
        url: 'COLOCAR URL DO CLP + /v1/sala',
        type: 'GET',
        dataType: 'json',
        success: function (obj) {
            Sala = obj;
        },
        error: function (xhr, errorString, exception) {
            alert("xhr.status=" + xhr.status + " error=" + errorString + " exception=" + exception);
        },
        BeforeSend: function (xhr) {
            xhr.withCredentials = true;
            xhr.setRequestHeader('Access-Control-Allow-Origin', '*');
            xhr.setRequestHeader('Access-Control-Allow-Credentials', '*');
        },
        crossDomain: true
    });
}

function exibirNome() {
	 var estado = "off";
    recuperarParametros();
    var valor = document.getElementById("labelPrincipal").innerHTML;
   

	//TODO Descomentar quando nao for teste
    //if (UsuarioLogado == undefined) {
    //    Session.clear();
    //    window.location = "login.html";
    //    return false;
    //}

    document.getElementById("labelPrincipal").innerHTML = valor + UsuarioLogado.nome;
}

function redirecionar(url) {
    Session.set("Sala", Sala);
    Session.set("UsuarioLogado", UsuarioLogado);
    Session.set("PautaRealizada", PautaRealizada);

    window.location = url;
}

function recuperarParametros() {
    Sala = Session.get("Sala");
    UsuarioLogado = Session.get("UsuarioLogado");
    PautaRealizada = Session.get("PautaRealizada");
}

function getXmlHttp() {
    var xmlHttp = null;
    if (typeof XMLHttpRequest != "undefined") {
        xmlHttp = new XMLHttpRequest();
    }
    else if (typeof window.ActiveXObject != "undefined") {
        try {
            xmlHttp = new ActiveXObject("Msxml2.XMLHTTP.4.0");
        }
        catch (e) {
            try {
                xmlHttp = new ActiveXObject("MSXML2.XMLHTTP");
            }
            catch (e) {
                try {
                    xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
                }
                catch (e) {
                    xmlHttp = null;
                }
            }
        }
    }

    return xmlHttp;
}

function fecharSala() {
    if (confirm("Realmente deseja fechar a sala?")) {
        var pautaRealizada = Session.get("PautaRealizada");

        if (pautaRealizada) {
            $.support.cors = true;
            $.ajax({
                url: 'COLOCAR URL DO CLP + /v1/ctrlsala',
                data: { identificacao: UsuarioLogado.identificacao },
                type: 'PUT',
                dataType: 'json',
                success: function (obj) {
                    Sala = obj;
                    
                    if (!Sala.salaAberta) {
                        redirecionar("sair.html");
                    }					
                },
                error: function (xhr, errorString, exception) {
                    alert("xhr.status=" + xhr.status + " error=" + errorString + " exception=" + exception);
                },
                BeforeSend: function (xhr) {
                    xhr.withCredentials = true;
                    xhr.setRequestHeader('Access-Control-Allow-Origin', '*');
                    xhr.setRequestHeader('Access-Control-Allow-Credentials', '*');
                },
                crossDomain: true
            });
        }
        else {
            alert("Favor realizar a pauta.");
            redirecionar("pauta.html");
        }
    }
}

function confirmarPauta() {
    if (confirm("Confirmar a presença dos alunos abaixo?")) {
        PautaRealizada = true;
        redirecionar("index.html");
    }
}

function carregarPauta() {
    recuperarParametros();
}

/**
 * Implements cookie-less JavaScript session variables
 * v1.0
 *
 * By Craig Buckler, Optimalworks.net
 *
 */
if (JSON && JSON.stringify && JSON.parse) var Session = Session || (function () {

    // window object
    var win = window.top || window;

    // session store
    var store = (win.name ? JSON.parse(win.name) : {});

    // save store on page unload
    function Save() {
        win.name = JSON.stringify(store);
    };

    // page unload event
    if (window.addEventListener) window.addEventListener("unload", Save, false);
    else if (window.attachEvent) window.attachEvent("onunload", Save);
    else window.onunload = Save;

    // public methods
    return {

        // set a session variable
        set: function (name, value) {
            store[name] = value;
        },

        // get a session value
        get: function (name) {
            return (store[name] ? store[name] : undefined);
        },

        // clear session
        clear: function () { store = {}; },

        // dump session data
        dump: function () { return JSON.stringify(store); }

    };

})();