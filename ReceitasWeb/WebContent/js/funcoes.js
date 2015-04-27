/**
 * Desenvolvimento das funções utilizadas pela aplicação web.
 */

function loadXMLDoc()
{
	var xmlhttp;
	
	if (window.XMLHttpRequest) // code for IE7+, Firefox, Chrome, Opera, Safari
	{ 
		xmlhttp = new XMLHttpRequest();
	}
	else // code for IE6, IE5
	{ 
		xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	
	xmlhttp.onreadystatechange = function()
	{
		if (xmlhttp.readyState==4 && xmlhttp.status==200)
	    {
			document.getElementById("myDiv").innerHTML=xmlhttp.responseText;
	    }
	}
	xmlhttp.open("GET", "http://localhost:8080/receitas-rs/hello", true);
	xmlhttp.send();
}

function validarCampoCountryCode()
{
	var ccc = document.forms[0].country_code;

	if (ccc.value.length != 3)
	{
		alert("O campo código do país aceita somente 3 caracteres");
		return false;	
	}
	
	var reg3letras=/[A-Z{3}]/;
	
	if (ccc.value.search(reg3letras) == -1)
	{
		alert("Por favor, informe três letras maiúsculas no campo Código do País");
		return false;
	}
	
	return true;
}

function validarCampos()
{
	if (validarCampoCountryCode())
	{
		alert("Tudo ok.. o formulário seria enviado");
	}
}


function isLetraMaiuscula(evento)
{
	evento = evento || window.event;
    var charCode = evento.which || evento.keyCode;
    var charStr = String.fromCharCode(charCode);
    if (/[A-Z]/.test(charStr))
        return true;
    
    return false;
}

/* 
 * função em javascript para fazer que a propriedade
 * maxlength faz
 */
function isTamanhoValido(campo)
{
	if (campo.value.length < 3)
		return false;

	return true;
}

function isValorValido(evento, campo) 
{ 				
	return true;

	if (!isLetraMaiuscula(evento))
		return false;

	if (!isTamanhoValido(campo))
		return false;
	
	return true;
} 