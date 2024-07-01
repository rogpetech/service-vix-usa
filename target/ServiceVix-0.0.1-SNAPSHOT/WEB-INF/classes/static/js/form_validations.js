$(document).ready(function () {


    // ----------------formvalidations----------------//
    const allForms = document.querySelectorAll('form')
    allForms.forEach((form)=>{
        
       form.addEventListener('submit', (e)=>{
        e.preventDefault()
            let current_form = e.target

           if (current_form.id =='form_prodct') {
                if(checkproductInputs()){
				}else{
					current_form.submit();;
				};
            } else if(current_form.id =='form_service'){
                if(checkserviceInputs()){}else{
					current_form.submit();
				};
            }else{
				current_form.submit();
			}
          
            
        })
        })
    
    // validations_functions//
    function setErrorFor(input, message) {
        const formControl = input.parentElement;
        const small = formControl.querySelector('small');
        formControl.className = 'input_field error';
        small.innerText = message;
    }

    function setSuccessFor(input) {
        const formControl = input.parentElement;
        formControl.className = 'input_field success';
    }
    // convertintocurrency//
    let USDollar = new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD',
    });

    $("#reg_price").change(function () {
        const reg_price = $('#reg_price').val();
        let converted_reg_price = USDollar.format(reg_price);
        $('#reg_price').val(converted_reg_price);
    });
    $("#mem_price").change(function () {
        const mem_price = $('#mem_price').val();
        let converted_mem_price = USDollar.format(mem_price);
        $('#mem_price').val(converted_mem_price);

    });
    $("#avg_cost").change(function () {
        const avg_cost = $('#avg_cost').val();
        let converted_avg_cost = USDollar.format(avg_cost);
        $('#avg_cost').val(converted_avg_cost);

    });

    // formvalidationsfor_addproductform//
    const inpproduct = document.getElementById('inpproduct');
    const reg_price = document.getElementById('reg_price');
    const mem_price = document.getElementById('mem_price');
    const avg_cost = document.getElementById('avg_cost');


    function checkproductInputs() {
        // trim to remove the whitespaces
        const inpproductValue = inpproduct.value.trim();
        const reg_priceValue = reg_price.value.trim();
        const mem_priceValue = mem_price.value.trim();
        const avg_costValue = avg_cost.value.trim();


        if (inpproductValue === '') {
            setErrorFor(inpproduct, 'Product name cannot be blank');

        } else if (inpproductValue.length < 4) {
            setErrorFor(inpproduct, 'Product name must be atlest 4 characteres');
        } else {
            setSuccessFor(inpproduct);
        }

        if (reg_priceValue === '') {
            setErrorFor(reg_price, 'Regular Price cannot be blank');
        } else {
            setSuccessFor(reg_price);
        }

        if (mem_priceValue === '') {
            setErrorFor(mem_price, 'Member Price cannot be blank');
        } else {
            setSuccessFor(mem_price);
        }

        if (avg_costValue === '') {
            setErrorFor(avg_cost, 'Average Cost cannot be blank');
        } else {
            setSuccessFor(avg_cost);
        }
    }



    // function isEmail(email) {
    // 	return /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(email);
    // }




    // formvalidationsfor_addserviceform//
    const inp_service_name = document.getElementById('inp_service_name');
    const reg_rate = document.getElementById('reg_rate');
    const mem_rate = document.getElementById('mem_rate');
    
    
    function checkserviceInputs() {
        // trim to remove the whitespaces
        const inp_service_nameValue = inp_service_name.value.trim();
        const reg_rateValue = reg_rate.value.trim();
        const mem_rateValue = mem_rate.value.trim();
     

        if (inp_service_nameValue === '') {
            setErrorFor(inp_service_name, 'Product name cannot be blank');

        } else if (inp_service_nameValue.length < 4) {
            setErrorFor(inp_service_name, 'Product name must be atlest 4 characteres');
        } else {
            setSuccessFor(inp_service_name);
        }

        if (reg_rateValue === '') {
            setErrorFor(reg_rate, 'Regular Rate cannot be blank');
        } else {
            setSuccessFor(reg_rate);
        }

        if (mem_rateValue === '') {
            setErrorFor(mem_rate, 'Regular Rate cannot be blank');
        } else {
            setSuccessFor(mem_rate);
        }

    }
   

});