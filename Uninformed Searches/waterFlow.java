/*Version 14 */
import java.io.*;
import java.util.*;
public class waterFlow{
	static boolean enableDebug=false;
	// The getInput method takes the input from text file specified in the CLA and stores it in ArrayList datastructure
	public static ArrayList<String> getInput(String[] args) throws IOException{
		String fileName = args[1];
		FileReader f = new FileReader(fileName);
		BufferedReader br = new BufferedReader(f);
		String line;
		ArrayList<String> list = new ArrayList<String>();
		while((line=br.readLine())!=null){
			/*if(enableDebug)System.out.println(line);*/
			list.add(line);
		}
		list.add("");
		return list;
	}
	public static void writeOutput(String outBFS,String outDFS, String outUCS) throws IOException{
		String oBFS = outBFS;
		String oDFS = outDFS;
		String oUCS = outUCS;
		File file= new File("output.txt");
		if(!file.exists())
			file.createNewFile();
		//file.getParentFile().mkdirs();
		PrintWriter writer = new PrintWriter(file,"UTF-8");
		if(!oBFS.equals(""))
			writer.println(oBFS);
		if(!oDFS.equals(""))
			writer.println(oDFS);
		if(!oUCS.equals(""))
			writer.println(oUCS);
		writer.close();
	}
	
	public static void main(String args[]) throws IOException{
		ArrayList<String> inputList = waterFlow.getInput(args);
		String[] inputStringArray = inputList.toArray(new String[0]);
		int numberOfTestCases = Integer.parseInt(inputStringArray[0]);
		int numberOfLines = inputStringArray.length;
		if(enableDebug)System.out.println(numberOfLines+" No of lines");
		String outputBFS="";
		String outputDFS="";
		String outputUCS="";
		int k=1;
		int nextIndex;
		int count=0;
		while(count<=numberOfTestCases-1){
			ArrayList<String> testCase = new ArrayList<String>();
		for(int i=k;i<numberOfLines;i++){
			if(!inputStringArray[i].equals("")){
				testCase.add(inputStringArray[i]);
			}
			else{
				if(testCase.size()==3){
					testCase.add("");
					k=i+1;
				}
				else{	
				//if(enableDebug)System.out.println(nextIndex);
				if(testCase.get(0).equals("BFS")){
					outputBFS = waterFlow.BFS(testCase);
					System.out.println("From main"+":"+outputBFS);
					writeOutput(outputBFS,outputDFS,outputUCS);
				}
				else if(testCase.get(0).equals("DFS")){
					outputDFS = waterFlow.DFS(testCase);
					System.out.println("From main"+":"+outputDFS);
					writeOutput(outputBFS,outputDFS,outputUCS);
				}
				else{
					outputUCS = waterFlow.UCS(testCase);
					System.out.println("From main"+":"+outputUCS);
					writeOutput(outputBFS,outputDFS,outputUCS);
				}
				nextIndex=i+1;
				count=count+1;
				k=nextIndex;
				break;
				}
			}		
		}
		}				
	}
	public static String BFS(ArrayList<String> testCase){
		if(enableDebug)System.out.println("Hello From BFS");
		//if(enableDebug)System.out.println("Size of test case list"+testCase.size());
		for(String str: testCase)
			if(enableDebug)System.out.println(str);
		//Get the nodes
		String sourceNode = testCase.get(1);
		if(enableDebug)System.out.println(sourceNode);
		String[] goalNodes = testCase.get(2).split("\\s+");
		for(String str: goalNodes)
			if(enableDebug)System.out.print(str);
		String[] middleNodes = testCase.get(3).split("\\s+");
		for(String str: middleNodes)
			if(enableDebug)System.out.print(str);
		//String[] x ="".split("\\s+");
		//for(String str: x)
		//	if(enableDebug)System.out.print(str);
		ArrayList<String> allNodes = new ArrayList<String>();
		allNodes.add(sourceNode);
		for(String nodes: goalNodes)
			allNodes.add(nodes);
		for(String nodes: middleNodes)
			allNodes.add(nodes);
		for(String str: allNodes)
		if(enableDebug)System.out.println("allnodes"+str);
		/*HashMap<String, HashMap<String, Integer>> map = new HashMap<String, HashMap<String, Integer>>();
		for(String firstNode:allNodes){
			map.put(firstNode, new HashMap<String,Integer>());
			for(String secondNode:allNodes){
				map.get(firstNode).put(secondNode,0);
			}
		}*/
		HashMap<String, HashMap<String, Integer>> adjMap = initializeAdjacencyMap(allNodes);
		int numberOfPipes = Integer.parseInt(testCase.get(4));
		if(enableDebug)System.out.println(numberOfPipes);
		for(int i=1;i<numberOfPipes+1;i++){
			String[] pipe = testCase.get(4+i).split("\\s+");
			String startPointOfPipe = pipe[0];
			String endPointOfPipe = pipe[1];
			updateAdjacencyMap(adjMap,startPointOfPipe,endPointOfPipe);
		}
		int startTime = Integer.parseInt(testCase.get(4+numberOfPipes+1));
		if(enableDebug)System.out.println(startTime);
		int finalTime;
		//Initialize Empty Explored Set
		HashMap<String,Boolean> exploredSet = new HashMap<String,Boolean>();
		for(String node: allNodes){
			exploredSet.put(node,false);
		}
		if(enableDebug)System.out.println("Goal Nodes are");
		for(String str: goalNodes)
			if(enableDebug)System.out.print(str+" ");
		//if(enableDebug)System.out.println("A".equals("A"));
		//boolean x = isGoal(goalNodes,"A");
		//if(enableDebug)System.out.println("Is x the goal? "+x);
		
		//Frontier Queue Initialization
		ArrayList<String> childNodes = new ArrayList<String>();
		boolean flag=true; 
		Queue<String> frontier = new LinkedList<String>();
		frontier.add(sourceNode);
		if(enableDebug)System.out.println("Before while loop"+frontier.size());
		HashMap<String,String> parent = new HashMap<String,String>();
		String parentNode;
		ArrayList<String> path = new ArrayList<String>();
		String output="";
		while(flag){
			if(frontier.isEmpty()){
				flag=false;
				if(enableDebug)System.out.println("None");
				output="None";
			}
			else{
				childNodes.clear();
				String frontNode=(String)frontier.poll();
				if(enableDebug)System.out.println("after removing front node"+frontier.size());
				exploredSet.replace(frontNode,true);
				for(String y:allNodes){
					if(adjMap.get(frontNode).get(y)==1)
						childNodes.add(y);
				}
				//sort childNodes alphabetically
				childNodes.sort((p1,p2)->p1.compareTo(p2));
				for(String child: childNodes){
					if(!(exploredSet.get(child)) && !(frontier.contains(child))){
						if(isGoal(goalNodes,child)){
							if(enableDebug)System.out.println("goal reached at:"+child);
							flag=false;
							parent.put(child,frontNode);
							if(enableDebug)System.out.println("Print path:");
							path.add(child);
							String temp = child;
							do{
								parentNode=parent.get(temp);
								path.add(parentNode);
								temp=parentNode;
							}while(!(parentNode.equals(sourceNode)));
							for(String p:path)
								if(enableDebug)System.out.print(p+"-");
							finalTime = startTime+path.size()-1;
							if(enableDebug)System.out.println("Time when water reaches"+" "+child+" is "+finalTime);
							if(enableDebug)System.out.println(path.size());
							path.clear();
							output=child+" "+finalTime;
							break;
						}
						frontier.add(child);
						parent.put(child,frontNode);
						flag=true;
					}			
				}		
			}
		}
		return output;
	}

	public static String DFS(ArrayList<String> testCase){
		if(enableDebug)System.out.println("Hello From DFS");
		String output="";
		for(String str: testCase)
			if(enableDebug)System.out.println(str);
		//Get the nodes
		String sourceNode = testCase.get(1);
		if(enableDebug)System.out.println(sourceNode);
		String[] goalNodes = testCase.get(2).split("\\s+");
		for(String str: goalNodes)
			if(enableDebug)System.out.print(str);
		String[] middleNodes = testCase.get(3).split("\\s+");
		for(String str: middleNodes)
			if(enableDebug)System.out.print(str);
		ArrayList<String> allNodes = new ArrayList<String>();
		allNodes.add(sourceNode);
		for(String nodes: goalNodes)
			allNodes.add(nodes);
		for(String nodes: middleNodes)
			allNodes.add(nodes);
		for(String str: allNodes)
		if(enableDebug)System.out.println("allnodes"+str);
		HashMap<String, HashMap<String, Integer>> adjMap = initializeAdjacencyMap(allNodes);
		int numberOfPipes = Integer.parseInt(testCase.get(4));
		if(enableDebug)System.out.println(numberOfPipes);
		for(int i=1;i<numberOfPipes+1;i++){
			String[] pipe = testCase.get(4+i).split("\\s+");
			String startPointOfPipe = pipe[0];
			String endPointOfPipe = pipe[1];
			updateAdjacencyMap(adjMap,startPointOfPipe,endPointOfPipe);
		}
		int startTime = Integer.parseInt(testCase.get(4+numberOfPipes+1));
		if(enableDebug)System.out.println(startTime);
		int finalTime;
		//Initialize Empty Explored Set
		HashMap<String,Boolean> exploredSet = new HashMap<String,Boolean>();
		for(String node: allNodes){
			exploredSet.put(node,false);
		}
		if(enableDebug)System.out.println("Goal Nodes are");
		for(String str: goalNodes)
			if(enableDebug)System.out.print(str+" ");
		
		//Frontier Stack Initialization
		ArrayList<String> childNodes = new ArrayList<String>();
		boolean flag=true; 
		Stack<String> frontier = new Stack<String>();
		frontier.push(sourceNode);
		HashMap<String,String> parent = new HashMap<String,String>();
		String parentNode;
		ArrayList<String> path = new ArrayList<String>();
		while(flag){
			if(frontier.empty()){
				flag=false;
				if(enableDebug)System.out.println("None");
				output="None";
			}
			else{
				childNodes.clear();
				String frontNode=(String)frontier.pop();
				if(enableDebug)System.out.println("after removing front node"+frontier.size());
				exploredSet.replace(frontNode,true);
				for(String y:allNodes){
					if(adjMap.get(frontNode).get(y)==1)
						childNodes.add(y);
				}
				//sort childNodes alphabetically
				childNodes.sort((p2,p1)->p1.compareTo(p2));
				for(String child: childNodes){
					if(!(exploredSet.get(child)) && !(frontier.contains(child))){
						if(isGoal(goalNodes,child)){
							if(enableDebug)System.out.println("goal reached at:"+child);
							flag=false;
							parent.put(child,frontNode);
							if(enableDebug)System.out.println("Print path:");
							path.add(child);
							String temp = child;
							do{
								parentNode=parent.get(temp);
								path.add(parentNode);
								temp=parentNode;
							}while(!(parentNode.equals(sourceNode)));
							for(String p:path)
								if(enableDebug)System.out.print(p+"-");
							finalTime = startTime+path.size()-1;
							if(enableDebug)System.out.println("Time when water reaches"+" "+child+" is "+finalTime);
							if(enableDebug)System.out.println(path.size());
							path.clear();
							output=child+" "+finalTime;
							break;
						}
					
					
						frontier.push(child);
						parent.put(child,frontNode);
						flag=true;
					}
			
					
					
				}
					
			}
			
		}
		return output;
		
	}
	
	static HashMap<String, Integer> pathCost = new HashMap<String, Integer>();
	static ArrayList<String> offTimeArray = new ArrayList<String>();
	static HashMap<String, HashMap<String, ArrayList<String>>> offTimes = new HashMap<String, HashMap<String, ArrayList<String>>>();
	static class PathLengthComparator implements Comparator<String>{
		public int compare(String a,String b){
			if(pathCost.get(a)<pathCost.get(b))
				return -1;
			else if(pathCost.get(a)>pathCost.get(b))
				return 1;
			else
				return a.compareTo(b);
	}
	
}
	public static String UCS(ArrayList<String> testCase){
		if(enableDebug)System.out.println("Hello From UCS");
		String output="";
		for(String str: testCase)
			if(enableDebug)System.out.println(str);
		//Get the nodes
		String sourceNode = testCase.get(1);
		if(enableDebug)System.out.println(sourceNode);
		String[] goalNodes = testCase.get(2).split("\\s+");
		for(String str: goalNodes)
			if(enableDebug)System.out.print(str);
		String[] middleNodes = testCase.get(3).split("\\s+");
		for(String str: middleNodes)
			if(enableDebug)System.out.print(str);
		ArrayList<String> allNodes = new ArrayList<String>();
		allNodes.add(sourceNode);
		for(String nodes: goalNodes)
			allNodes.add(nodes);
		for(String nodes: middleNodes)
			allNodes.add(nodes);
		for(String str: allNodes)
		if(enableDebug)System.out.println("allnodes"+str);
		HashMap<String, HashMap<String, Integer>> adjMap = initializeAdjacencyMap(allNodes);
		int numberOfPipes = Integer.parseInt(testCase.get(4));
		if(enableDebug)System.out.println(numberOfPipes);
		//HashMap<String, HashMap<String, ArrayList<String>>> offTimes = new HashMap<String, HashMap<String, ArrayList<String>>>();
		//ArrayList<String> offTimeArray = new ArrayList<String>();
		
		for(String m:allNodes){
			offTimes.put(m,new HashMap<String,ArrayList<String>>());
			for(String n:allNodes){
				offTimes.get(m).put(n,offTimeArray);
				
			}
		}
		//if(enableDebug)System.out.println(offTimes.get("AA").get("BA"));
		for(int i=1;i<numberOfPipes+1;i++){
			//offTimeArray.clear();
			ArrayList<String> offTimeArray = new ArrayList<String>();
			String[] pipe = testCase.get(4+i).split("\\s+");
			String startPointOfPipe = pipe[0];
			String endPointOfPipe = pipe[1];
			int pathLength = Integer.parseInt(pipe[2]);
			updateAdjacencyMapUCS(adjMap,startPointOfPipe,endPointOfPipe,pathLength);
			//Storing off times
			
			int numberOfOffTimes = Integer.parseInt(pipe[3]);
			//if(enableDebug)System.out.println(numberOfOffTimes);
			if(numberOfOffTimes!=0){
				for(int j=0;j<numberOfOffTimes;j++){
					String temp = pipe[4+j]; 
					offTimeArray.add(temp);	
				}
			}
			if(enableDebug)System.out.println("Start:"+startPointOfPipe+" End:"+endPointOfPipe+" OfftimeArray: ");
			//for(String str: offTimeArray){
			//	if(enableDebug)System.out.print(str+ "");
			//}
			//offTimes.put(startPointOfPipe, new HashMap<String,ArrayList<String>>());
			offTimes.get(startPointOfPipe).put(endPointOfPipe,offTimeArray);
			for(String str:offTimes.get(startPointOfPipe).get(endPointOfPipe))
				if(enableDebug)System.out.print(str+ " ");
		}
		
		
		int startTime = Integer.parseInt(testCase.get(4+numberOfPipes+1));
		//if(enableDebug)System.out.println(startTime);
		//HashMap<String, Integer> pathCost = new HashMap<String, Integer>();
		for(String str: allNodes){
			pathCost.put(str,0);
		}
		//Initialize Empty Explored Set
		HashMap<String,Boolean> exploredSet = new HashMap<String,Boolean>();
		for(String node: allNodes){
			exploredSet.put(node,false);
		}
		
		//Initialize Frontier
		
		Comparator<String> comp = new PathLengthComparator();
		PriorityQueue<String> frontier = new PriorityQueue<String>(comp);
		//Uniform cost Search Algorithm
		
		frontier.add(sourceNode);
		if(enableDebug)System.out.println("Frontier initially with source node:"+frontier);
		boolean flag =true;
		ArrayList<String> childNodes = new ArrayList<String>();
		// parent
		HashMap<String,String> parent = new HashMap<String,String>();
		String parentNode;
		ArrayList<String> path = new ArrayList<String>();
		//parent.put(sourceNode,sourceNode);
		int currentTime = startTime;
		pathCost.put(sourceNode,currentTime);
		if(enableDebug)System.out.println("Current Time is:"+currentTime);
		while(flag){
			if(frontier.isEmpty()){
				flag=false;
				if(enableDebug)System.out.println("None");
				output="None";
			}
			else{
				childNodes.clear();
				ArrayList<String> offTime = new ArrayList<String>();
				//String peekFront=(String)frontier.peek();
				//String peekParent = parent.get(peekFront);
				//ArrayList<String> offTime = offTimes.get(peekParent).get(peekFront);
					if(enableDebug)System.out.println("Frontier after before frontNode:"+frontier);
					String frontNode=(String)frontier.poll();
					if(enableDebug)System.out.println("Frontier after popping frontNode:"+frontier);
					if(enableDebug)System.out.println("Frontnode:"+frontNode);
					if(enableDebug)System.out.println("after removing front node"+frontier.size());
					if(isGoal(goalNodes,frontNode)){
						flag=false;
						
						if(enableDebug)System.out.println("Goal at:"+frontNode);
						if(enableDebug)System.out.println("Path cost of goal is:"+pathCost.get(frontNode));
						int pathCostUCS = pathCost.get(frontNode);
						if(pathCostUCS>=24){
							currentTime=pathCostUCS%24;
						}
						else{
							currentTime=pathCostUCS;
						}
						if(enableDebug)System.out.println("Time at which water reaches destination:"+currentTime);
						//Return path,pathcost and goalnode
						output=frontNode+" "+currentTime;
					}
					else{
						
						//if(isValidTime(currentTime,offTime) || frontNode.equals(sourceNode)){
							
							if(enableDebug)System.out.println("Current time before traversing a valid path:"+currentTime);
							exploredSet.replace(frontNode,true);
							if(!frontNode.equals(sourceNode)){
								currentTime = pathCost.get(frontNode);
							}
							
							if(enableDebug)System.out.println("Current time after traversing a valid path:"+currentTime);
							for(String y:allNodes){
								if(adjMap.get(frontNode).get(y)!=0)
									childNodes.add(y);
							}
							//for(String cn:childNodes){
								//if(enableDebug)System.out.print("children of:"+frontNode);
								//if(enableDebug)System.out.println(cn);
							//}
							for(String child:childNodes){
								if(!(exploredSet.get(child)) && !(frontier.contains(child))){
									//add it only if pipe from parent to child active starting from the next moment
									//if(enableDebug)System.out.println(offTimes.get(frontNode).get(child));
									for(String z:offTimes.get(frontNode).get(child))
										if(enableDebug)System.out.print(z+ "xxx ");
									if(enableDebug)System.out.println(offTimes.get(frontNode).get(child));
									boolean x = isValidTime(currentTime,offTimes.get(frontNode).get(child));
									if(enableDebug)System.out.println("Child:"+child+" validtime:"+x);
									if(isValidTime(currentTime,offTimes.get(frontNode).get(child))){
										frontier.add(child);
										int edgeCost = adjMap.get(frontNode).get(child);
										int parentPathCost = pathCost.get(frontNode);
										int updatedPathCost = parentPathCost+edgeCost;
										if(enableDebug)System.out.println("Edgecost for"+frontNode+" and "+child+"is "+edgeCost);
										if(enableDebug)System.out.println("Existing path cost for "+child+" is "+parentPathCost);
										if(enableDebug)System.out.println("Updated path cost for "+child+"is "+updatedPathCost);
										pathCost.put(child,updatedPathCost);
										parent.put(child,frontNode);
										flag=true;
									}
								}
								else if(frontier.contains(child)){
									int existingPathCost=pathCost.get(child);
									int newPathCost= pathCost.get(frontNode)+adjMap.get(frontNode).get(child);
									if(existingPathCost>newPathCost){
										
										if(isValidTime(currentTime, offTimes.get(frontNode).get(child))){
											frontier.remove(child);
											pathCost.put(child,newPathCost);
											parent.put(child,frontNode);
											frontier.add(child);
											flag = true;
										}
									}
									flag=true;
								}
								else{}
							}
						//}
						
						
					}//else ending
			
			
				
				
				
					
			}
			
		}
		
		return output;
		
	}
	
	public static HashMap<String, HashMap<String, Integer>> initializeAdjacencyMap(ArrayList<String> allNodes){
			HashMap<String, HashMap<String, Integer>> map = new HashMap<String, HashMap<String, Integer>>();
			for(String firstNode:allNodes){
			map.put(firstNode, new HashMap<String,Integer>());
			for(String secondNode:allNodes){
				map.get(firstNode).put(secondNode,0);
			}
			}
			return map;
		}
	
	public static HashMap<String, HashMap<String, Integer>> updateAdjacencyMap(HashMap<String, HashMap<String, Integer>> map, String firstNode, String secondNode){
			map.get(firstNode).put(secondNode,1);
			return map;
		}
		
	public static HashMap<String, HashMap<String, Integer>> updateAdjacencyMapUCS(HashMap<String, HashMap<String, Integer>> map, String firstNode, String secondNode, int value){
			int currentValue = map.get(firstNode).get(secondNode);
			int updatedValue = currentValue + value;
			map.get(firstNode).put(secondNode,updatedValue);
			return map;
		}	
	
	public static boolean isGoal(String[] goalNode, String node){
		boolean goal=false;
		for(String str: goalNode){
			if(str.equals(node)){
				goal= true;
				break;
			}
			else{
				goal= false;
			}
		}
		return goal;
	}
	
	public static boolean isValidTime(int curr, ArrayList<String> offTimes){
		boolean valid=false;
		int current=curr;
		if(current>=24){
			current=current%24;
		}
		for(String str:offTimes){
			if(enableDebug)System.out.println("OFFTIMES FROM ISVALID "+str);
		}
		if(!offTimes.isEmpty()){
			for(String str:offTimes){
				String[] number = str.split("-");
				int min = Integer.parseInt(number[0]);
				int max =Integer.parseInt(number[1]);
				if(enableDebug)System.out.println("Max is"+max+" and min is"+min);
				if(enableDebug)System.out.println("Current time is:"+current);
				if(current>=min && current<=max){
					valid= false;
					if(enableDebug)System.out.println("VALID");
					break;
				}
				else{
					valid=true;
				}
			
			}
		}
		else{
			valid=true;
		}
		if(enableDebug)System.out.println("valid is"+valid);
		return valid;
	}
	
	
}
		
	
	



