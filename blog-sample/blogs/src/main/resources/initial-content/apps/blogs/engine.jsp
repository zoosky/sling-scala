<%@page import="javax.jcr.Session"%>
<%@page import="javax.jcr.Node"%>
<%@page import="javax.jcr.NodeIterator"%>
<%@page import="javax.jcr.RepositoryException"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.*"%>

<%!

	public class BlogEngine{

	

	public Node addNodeRecursively(String path, String type, Session session) throws Exception {

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

		public String title;
		public String body;
		public String time;
		public String year;
		public String month;
		public String resourcePath;

	}

	public class Comment{

		public String name;
		public String webLink;
		public String Comment;
		
	}


	public class TreeNode{

		private ArrayList childrenHolder = new ArrayList();
		private ArrayList postHolder = new ArrayList() ;

		private String year = "xxxx";
		private String month = "xxx";
		private String name = "un assigned";

		public String getName(){
			return name;
		}

		public void setName(String s){
			name = s;
		}
		

		public void addChild(TreeNode tn){
			childrenHolder.add(tn);
		}

		public boolean containsChild(String val){
			boolean ret = false;
			if(!childrenHolder.isEmpty()){
				for(int i=0 ; i<childrenHolder.size() ; i++){
					TreeNode t = (TreeNode)childrenHolder.get(i);
					if(t.getName().equals(val)){
						ret = true;
					}
				}
			}
			return ret;
		}
			
		public boolean isEmpty(){

			if(childrenHolder.isEmpty()){
				return true;
			}else{
				return false;
			}

		}
	
			
		public void addPost(Post p){
			postHolder.add(p);
		}

		public ArrayList getPostList(){
			return postHolder;
		}

		public ArrayList getChildList(){
			return childrenHolder;
		}

		public TreeNode getChild(String val){

			TreeNode tt=null;
			if(!childrenHolder.isEmpty()){
				for(int i=0 ; i<childrenHolder.size() ; i++){
					TreeNode t = (TreeNode)childrenHolder.get(i);
					if(t.getName().equals(val)){
						tt = t;
					}
				}
			}
			return tt;

		}

			

	}


	public class PostTreeControler{

		private TreeNode root = new TreeNode();

		public TreeNode createPostTree(NodeIterator ni){

			try{
			
			if(ni.hasNext()) {
				while(ni.hasNext()) {
					Node n = ni.nextNode();
					String time = n.getProperty("time").getString();

					String [] split_date = time.split(" ");

					String year = split_date[5];
					String prefix = split_date[1];

					if(prefix.equals("Jan")){
						prefix = "January";
					}else if(prefix.equals("Feb")){
						prefix = "February";
					}else if(prefix.equals("Mar")){
						prefix = "March";
					}else if(prefix.equals("Apr")){
						prefix = "April";
					}else if(prefix.equals("May")){
						prefix = "May";
					}else if(prefix.equals("Jun")){
						prefix = "June";
					}else if(prefix.equals("Jul")){
						prefix = "July";
					}else if(prefix.equals("Aug")){
						prefix = "August";
					}else if(prefix.equals("Sep")){
						prefix = "September";
					}else if(prefix.equals("Oct")){
						prefix = "October";
					}else if(prefix.equals("Nov")){
						prefix = "November";				
					}else if(prefix.equals("Dec")){
						prefix = "December";
					}

					//ArrayList monthList = root.getChildList();

					if(!root.isEmpty()){
						if(root.containsChild(prefix)){
							TreeNode month = root.getChild(prefix);
							if(month.containsChild(year)){
								TreeNode y = month.getChild(year);
								Post p = new Post();
								p.title = n.getProperty("title").getString();
								p.body = n.getProperty("body").getString();
								p.time = n.getProperty("time").getString();
								p.year = year;
								p.month = prefix;
								p.resourcePath = n.getPath();
								y.addPost(p);
							}
							else{
								TreeNode y = new TreeNode();
								y.setName(year);
								Post p = new Post();
								p.title = n.getProperty("title").getString();
								p.body = n.getProperty("body").getString();
								p.time = n.getProperty("time").getString();
								p.year = year;
								p.month = prefix;
								p.resourcePath = n.getPath();
								y.addPost(p);
								month.addChild(y);
							}
						}else{
							TreeNode m = new TreeNode();
							m.setName(prefix);
							TreeNode y = new TreeNode();
							y.setName(year);
							Post p = new Post();
							p.title = n.getProperty("title").getString();
							p.body = n.getProperty("body").getString();
							p.time = n.getProperty("time").getString();
							p.year = year;
							p.month = prefix;
							p.resourcePath = n.getPath();
							y.addPost(p);
							m.addChild(y);
							root.addChild(m);
						}
					}else{
						
						TreeNode m = new TreeNode();
						m.setName(prefix);
						TreeNode y = new TreeNode();
						y.setName(year);
						Post p = new Post();
						p.title = n.getProperty("title").getString();
						p.body = n.getProperty("body").getString();
						p.time = n.getProperty("time").getString();
						p.year = year;
						p.month = prefix;
						p.resourcePath = n.getPath();
						y.addPost(p);
						m.addChild(y);
						root.addChild(m);
					}
				}
			}
			}catch(Exception e){
				e.printStackTrace();
			}

			return root;
		}


		public ArrayList getPostList(String month , String year){
			
			ArrayList pList=null;

			if(!root.isEmpty()){
				if(root.containsChild(month)){
					TreeNode m = root.getChild(month);
					if(m.containsChild(year)){
						TreeNode y = m.getChild(year);
						pList=y.getPostList();
					}
				}
			}
			return pList;
		}


									
							
							
	}		

%>
