/**
 * 
 */

let $ = (selector,text) =>{
	if(text){
		document.querySelector(selector).innerHTML += `${text}<br>` ;
	}
 	return document.querySelector(selector);
}

