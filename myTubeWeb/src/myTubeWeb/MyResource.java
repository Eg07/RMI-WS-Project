package myTubeWeb;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;

import javax.ws.rs.GET;
import javax.ws.rs.POST;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import model.Content;
import model.User;
import rmiServer.ContentServiceImpl;

@RequestScoped
@Path("")
@Produces("application/json")
@Consumes("application/json")
public class MyResource {

	@Path("/all")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response returnAllContent() throws Exception {
		String returnString = null;
		Gson gsonBuilder = new GsonBuilder().create();
		List<Content> list = new ArrayList<>();
		try {

			ContentServiceImpl dao = new ContentServiceImpl();

			list = dao.getAllContent();

			returnString = gsonBuilder.toJson(list);

		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500).entity("Server was not able to process your request !").build();

		}
		return Response.ok(returnString).build();
	}

	@Path("/topic")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getByTopic(@QueryParam("topic") String topic) throws Exception {
		String returnString = null;
		Gson gsonBuilder = new GsonBuilder().create();
		List<Content> list = new ArrayList<>();
		try {
			if (topic == null)
				return Response.status(400).entity("Error: please enter the topic for search !").build();

			ContentServiceImpl dao = new ContentServiceImpl();

			list = dao.getByTopic(topic);

			returnString = gsonBuilder.toJson(list);

		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500).entity("Server was not able to process your request !").build();

		}
		return Response.ok(returnString).build();
	}

	@Path("/search")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response search(@QueryParam("title") String title) throws Exception {
		String returnString = null;
		Gson gsonBuilder = new GsonBuilder().create();
		List<Content> list = new ArrayList<>();
		try {
			if (title == null)
				return Response.status(400).entity("Error: please enter the topic for search !").build();

			ContentServiceImpl dao = new ContentServiceImpl();

			list = dao.searchWs(title);
			returnString = gsonBuilder.toJson(list);

		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500).entity("Server was not able to process your request !").build();

		}
		return Response.ok(returnString).build();
	}

	@Path("/post")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addContent(Content content) throws Exception {

		ContentServiceImpl dao = new ContentServiceImpl();

		try {

			dao.insertConent(content);

		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500).entity("Server was not able to process your request !").build();
		}

		return Response.ok("a new content !").build();
	}

	@Path("/delete")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	// @Consumes(MediaType.APPLICATION_JSON)
	public Response delete(@QueryParam("id") String id) throws Exception {
		int status = 0;
		ContentServiceImpl dao = new ContentServiceImpl();
		try {
			if (id == null)
				return Response.status(400).entity("Error: please enter content's id !").build();

			status = dao.deleteContent(Integer.parseInt(id));
			
			if(status != 0)
				return Response.ok("The content has been removed !").build();
			else 
				return Response.ok("You can't remove the content  !").build();

		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500).entity("Server was not able to process your request !").build();
		}
		//return Response.ok("The content has been removed !").build();
	}

	@Path("/update")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateContent(@QueryParam("title") String title, @QueryParam("topic") String topic,
			@QueryParam("id") String id) throws Exception {

		ContentServiceImpl dao = new ContentServiceImpl();
		int status = 0;
		try {
			if (id == null)
				return Response.status(400).entity("Error: please enter content's id !").build();

			status = dao.updateContentWs(title, topic, Integer.parseInt(id));
			
			if(status != 0)
				return Response.ok("The content has been updated !").build();
			else 
				return Response.ok("You can't update the content  !").build();

		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500).entity("Server was not able to process your request !").build();
		}

		//return Response.ok("The content has been updated!").build();
	}

	@GET
	@Path("/getId")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadById(@QueryParam("id") String id, @QueryParam("extension") String extension) throws Exception {

		ContentServiceImpl dao = new ContentServiceImpl();
		try {
			if (id == null)
				return Response.status(400).entity("Error: please enter content's id !").build();

			dao.getContentById(Integer.parseInt(id), extension);

		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500).entity("Server was not able to process your request !").build();

		}
		return Response.ok("The content has been downloaded it \n Go to download Dir !").build();
	}
	
	@GET
	@Path("/getName")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadByName(@QueryParam("name") String name , @QueryParam("extension") String extension) throws Exception {

		ContentServiceImpl dao = new ContentServiceImpl();
		try {
			if (name == null)
				return Response.status(400).entity("Error: please enter content's id !").build();

			dao.getContentByName(name,extension);

		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500).entity("Server was not able to process your request !").build();

		}
		return Response.ok("The content has been downloaded it \n Go to download Dir !").build();
	}
	
	@GET
	@Path("/checkUser")
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkUser(@QueryParam("name") String name, @QueryParam("password") String password) throws Exception {

		ContentServiceImpl dao = new ContentServiceImpl();
		try {
			if (name == null)
				return Response.status(400).entity("Error: please enter user's name !").build();
			
			if (password == null)
				return Response.status(400).entity("Error: please enter user's password !").build();
			
			User user = new User(name,password);
			int temp = dao.checkUser(user);
			
			if (temp != 0 ) {
				return Response.ok(temp).build();
			}else 
				return Response.ok("error the user is not existing !").build();

		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500).entity("Server was not able to process your request !").build();

		}
		//return Response.ok("Log in !!! ").build();
	}
	
	@Path("/addUser")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addUser(User user) throws Exception {

		ContentServiceImpl dao = new ContentServiceImpl();

		try {

			dao.addUser(user);

		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500).entity("Server was not able to process your request !").build();
		}

		return Response.ok("a new user  !").build();
	}
}
