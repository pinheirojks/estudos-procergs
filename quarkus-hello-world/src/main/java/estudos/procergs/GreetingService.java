package estudos.procergs;

import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class GreetingService {

    public String hello(){
        return "Olá mundo from service!!!";
    }
    
}
