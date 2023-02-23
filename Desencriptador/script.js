const textarea = document.querySelector(".text-area");
const mensaje = document.querySelector(".mensaje");


function btnEncriptar(){
    const textEncriptado = encriptar(textarea.value);
    mensaje.value =  textEncriptado;
    textarea.value = "";
    mensaje.style.backgroundImage = "none";
}

function encriptar(StringEncriptado){
    let matrizcodigo = [["e", "enter"], ["i", "imes"], ["a", "ai"], ["o", "ober"], ["u","ufat"]];
    StringEncriptado = StringEncriptado.toLowerCase();

    for(let i = 0; i < matrizcodigo.length; i++){
        if(StringEncriptado.incluedes(matrizcodigo[i][0])){
            StringEncriptado = StringEncriptado.replaceAll(matrizcodigo[i][0], matrizcodigo[i][1]);
            
        }
    }
    return StringEncriptado;
}
