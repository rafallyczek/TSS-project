package webapplication.restfulservice;

import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import webapplication.dao.DAO;
import webapplication.model.User;

@Path("restservice")
public class RESTfulService {

    @Context
    private UriInfo context;

    public RESTfulService() {
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getJson() {
        DAO dao = new DAO();
        List<User> list = dao.getAllUsers();
        return list;
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
    
}
