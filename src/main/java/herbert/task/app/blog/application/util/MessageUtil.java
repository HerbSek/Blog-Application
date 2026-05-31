/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package herbert.task.app.blog.application.util;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

/**
 *
 * @author HerbertSekpey
 */

public class MessageUtil {
    
    public FacesContext context() {
        return FacesContext.getCurrentInstance();
    }

      public void keepMessages() {
        context().getExternalContext()
             .getFlash()
             .setKeepMessages(true);
    }

    public void messageSuccess(String clientId, String headline, String details) {
            context().addMessage(clientId,
                new FacesMessage(FacesMessage.SEVERITY_INFO, headline, details));
    }

    public void messageFailure(String clientId, String headline, String details) {
       context().addMessage(clientId,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, headline, details));
    }

    public void messageInfo(String clientId, String headline, String details) {
        context().addMessage(clientId,
                new FacesMessage(FacesMessage.SEVERITY_WARN, headline, details));
    }
}