<%@page import="javax.jcr.Session"%>
<%@page import="javax.jcr.Node"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util"%>

<%!

	public class BlogEngine{

	

	public static Node addNodeRecursively(String path, String type, Session session) throws Exception {
		Node currentRoot = session.getRootNode();
		String[] pathSegments = path.split("/");
		for(int i = 1; i < pathSegments.length; i++) {			
			if(!currentRoot.hasNode(pathSegments[i])) {
				currentRoot.addNode(pathSegments[i], "nt:unstructured");
			}
			currentRoot = currentRoot.getNode(pathSegments[i]);
		}
		return currentRoot;
	}



	}


	public class Post{

		public string title;
		public string body;
		public string time;

	}

	public class NodeList{

		private ArrayList yearList = new Arraylist();

		private ArrayList janList = new Arraylist();
		private ArrayList febList = new Arraylist();
		private ArrayList marList = new Arraylist();
		private ArrayList aprList = new Arraylist();
		private ArrayList mayList = new Arraylist();
		private ArrayList junList = new Arraylist();
		private ArrayList julList = new Arraylist();
		private ArrayList augList = new Arraylist();
		private ArrayList sepList = new Arraylist();
		private ArrayList octList = new Arraylist();
		private ArrayList novList = new Arraylist();
		private ArrayList decList = new Arraylist();
		
		public static ArrayList getYearList(){
			return yearList;
		}

		public static void setYearList(Node n){
			yearList.Add(n);
		}

		public static ArrayList getYearList(){
			return monthList;
		}

		public static void setYearList(Node n){
			monthList.Add(n);
		}

		public static createPostList(NodeIterator ni){

			while(ni.hasNext()) {
				Node n = ni.nextNode();
				Post p = new Post();
				p.title = n.getProperty("title").getString();	
				p.body = n.getProperty("title").getString();	
				p.time = n.getProperty("title").getString();	
				
				string temp = p.time;
				string[] split = temp.split(" ");
				
			}
		}

	}		

%>
