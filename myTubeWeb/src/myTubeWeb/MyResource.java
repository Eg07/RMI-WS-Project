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

		ContentServiceImpl dao = new ContentServiceImpl();
		try {
			if (id == null)
				return Response.status(400).entity("Error: please enter content's id !").build();

			dao.deleteContent(Integer.parseInt(id));

		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500).entity("Server was not able to process your request !").build();

		}
		return Response.ok("The content has been removed !").build();
	}

	@Path("/update")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateContent(@QueryParam("title") String title, @QueryParam("topic") String topic,
			@QueryParam("id") String id) throws Exception {

		ContentServiceImpl dao = new ContentServiceImpl();

		try {
			if (id == null)
				return Response.status(400).entity("Error: please enter content's id !").build();

			dao.updateContentWs(title, topic, Integer.parseInt(id));

		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500).entity("Server was not able to process your request !").build();
		}

		return Response.ok("The content has been updated!").build();
	}

	@GET
	@Path("/getId")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadById(@QueryParam("id") String id) throws Exception {

		ContentServiceImpl dao = new ContentServiceImpl();
		try {
			if (id == null)
				return Response.status(400).entity("Error: please enter content's id !").build();

			dao.getContentById(Integer.parseInt(id));

		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500).entity("Server was not able to process your request !").build();

		}
		return Response.ok("The content has been downloaded it \n Go to download Dir !").build();
	}
	
	@GET
	@Path("/getName")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadByName(@QueryParam("name") String name) throws Exception {

		ContentServiceImpl dao = new ContentServiceImpl();
		try {
			if (name == null)
				return Response.status(400).entity("Error: please enter content's id !").build();

			dao.getContentByName(name);

		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500).entity("Server was not able to process your request !").build();

		}
		return Response.ok("The content has been downloaded it \n Go to download Dir !").build();
	}
}
