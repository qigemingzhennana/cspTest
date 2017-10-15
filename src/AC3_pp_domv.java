public class AC3_pp_domv
{
	DATA data=null;
	
	  int rm[][][]=null;
	
	int vdense[][]=null;
	int vsparse[][]=null;
	int vcurlevel[][]=null;
	
	int level=0;
	
	
	int vcur[]=null;//变量[a]的论域大小
	
	
	int	levelvdense[]=null;
	int	levelvsparse[]=null;
	//
	int vtoc[][]=null;
	int vtocn[]=null;
	
	  long    vvT[]=null;
	  long	 cT[]=null;
	  long    backT=0;
	  int    heap[]=null;
	  int    heapn=0;
	  int    inheap[]=null;
	  
	int    assignedcount[]=null;
	int maxd=0;
	
	boolean relMap[][][][]=null;

	public AC3_pp_domv(DATA indata)
	{
		data=indata;
		
		for(int i=0;i<data.varN;i++)
		{
			if(data.domn[i]>maxd)
			{
				maxd=data.domn[i];
			}
		}
		
		////////////////////////////////////////////////////////////////////////////////////////
		vdense=new int[data.varN][];
		vcurlevel=new int[data.varN+1][data.varN];
		vsparse=new int[data.varN][];
		vcur=new int[data.varN];
		for(int i=0;i<data.varN;i++)
		{
			vdense[i]=new int[data.domn[i]];
			vsparse[i]=new int[data.domn[i]];
			vcur[i]=data.domn[i]-1;
			
			for(int j=0;j<data.domn[i];j++)
			{
				vdense[i][j]=j;
				vsparse[i][j]=j;
			}
		}
		
		///////////////////////////////////////////////////////////////////
		assignedcount=new int[data.constraintN];

		int support[][][][]=new int[data.constraintN][2][][];
		 rm=new int[data.varN][data.varN][];
		 for(int i=0;i<data.constraintN;i++)
		 {
			 //support[i]=new int[data.scopeN[i]][][];
			 int rel[][]=data.relations[i];
			 
			 int x=data.scopes[i][0];
			 int y=data.scopes[i][1];
			 
			 support[i][0]=new int[data.domn[x]][];
			 support[i][1]=new int[data.domn[y]][];
			 
			 rm[x][y]=new int[data.domn[x]];
			 rm[y][x]=new int[data.domn[y]];
			 
			 int xN[]=new int[data.domn[x]];
			 int yN[]=new int[data.domn[y]];
			 
			 for(int j=0;j<rel[0].length;j++)
			 {
				 xN[rel[0][j]]++;
				 yN[rel[1][j]]++;
			 }
			 
			 for(int j=0;j<data.domn[x];j++)
			 {
				 rm[x][y][j]=-1;
				 support[i][0][j]=new int[xN[j]];
				 xN[j]=0;
			 }
			 for(int j=0;j<data.domn[y];j++)
			 {
				 rm[y][x][j]=-1;
				 support[i][1][j]=new int[yN[j]];
				 yN[j]=0;
			 }
			 
			 for(int j=0;j<rel[0].length;j++)
			 {
				 int a=rel[0][j];
				 int b=rel[1][j];
				 
				 support[i][0][a][xN[a]]=b;
				 xN[a]++;
				 support[i][1][b][yN[b]]=a;
				 yN[b]++;
			 }
			 
			 for(int j=vcur[x];j>=0;j--)
			 {
				 int a=vdense[x][j];
				 if(support[i][0][a].length>0)
				 {
					 rm[x][y][a]=support[i][0][a][0];
				 }
				 else
				 {
						int b=vdense[x][vcur[x]];
						vdense[x][j]=b;
						vsparse[x][b]=j;
						
						vdense[x][vcur[x]]=a;
						vsparse[x][a]=vcur[x];
						
						vcur[x]--;
				 }
			 }
			 for(int j=vcur[y];j>=0;j--)
			 {
				 int a=vdense[y][j];
				 if(support[i][1][a].length>0)
				 {
					 rm[y][x][a]=support[i][1][a][0];
				 }
				 else
				 {
						int b=vdense[y][vcur[y]];
						vdense[y][j]=b;
						vsparse[y][b]=j;
						
						vdense[y][vcur[y]]=a;
						vsparse[y][a]=vcur[y];
						
						vcur[y]--;
				 }
			 }
		 }

		///////////////////////////////////////////////////////////////////////////////////////////////////////////////
		 levelvdense=new int[data.varN];
		 levelvsparse=new int[data.varN];
		 
		 for(int i=0;i<data.varN;i++)
		 {
			 levelvdense[i]=i;
			 levelvsparse[i]=i;
		 }	
		/////////////////////////////////////////////////////////////////////////////////////////////
	     heap=new int[data.varN];
	     inheap=new int[data.varN];
	     cT=new long[data.constraintN];
	     vvT=new long[data.varN];
	     
		//
		vtocn=new int[data.varN];
		vtoc=new int[data.varN][];
		
		for(int i=0;i<data.constraintN;i++)
		{
			int x=data.scopes[i][0];
			int y=data.scopes[i][1];
			
			vtocn[x]++;
			vtocn[y]++;
		}
		
		for(int i=0;i<data.varN;i++)
		{
			vtoc[i]=new int[vtocn[i]];
			vtocn[i]=0;
		}
		
		for(int i=0;i<data.constraintN;i++)
		{
			int x=data.scopes[i][0];
			int y=data.scopes[i][1];
		
			vtoc[x][vtocn[x]]=i;
			vtoc[y][vtocn[y]]=i;
			
			vtocn[x]++;
			vtocn[y]++;
		}
		////////////////////////////////////////////////////
		relMap=new boolean[data.varN][data.varN][][];
		for(int i=0;i<data.constraintN;i++)
		{
			int rel[][]=data.relations[i];
			int x=data.scopes[i][0];
			int y=data.scopes[i][1];
			relMap[x][y]=new boolean[data.domn[x]][data.domn[y]];
			relMap[y][x]=new boolean[data.domn[y]][data.domn[x]];
			for(int j=0;j<rel[0].length;j++)
			{
				int a=rel[0][j];
				int b=rel[1][j];
				relMap[x][y][a][b]=true;
				relMap[y][x][b][a]=true;
			}
		}
		
		for(int i=0;i<data.varN;i++)
		{
			for(int j=vcur[i];j>=0;j--)
			{
				int a=vdense[i][j];
				for(int r=0;r<vtocn[i];r++)
				{
					int c=vtoc[i][r];
					int vinc=0;
					if(data.scopes[c][0]!=i)
					{
						vinc=1;
					}
					int s[]=support[c][vinc][a];
					if(s.length==0)
					{
						System.out.println("dddddddddddddddddddddddddddddddddddddddddddddd");
						int b=vdense[i][vcur[i]];
						vdense[i][j]=b;
						vsparse[i][b]=j;
						
						vdense[i][vcur[i]]=a;
						vsparse[i][a]=vcur[i];
						
						vcur[i]--;
						break;
					}
				}
			}
		}
	}
	
	boolean MAC()
	{
		heapn=0;
		  backT=1;
		  for(int i=0;i<data.varN;i++)
		  {
			  addheap(i);
		  }
		
		level=0;
		
		int n=0;
		long time=0;
		long time1=System.currentTimeMillis();
		  
		while(level>=0)
		{
			  time=System.currentTimeMillis();
			  if((time-time1)>30000)
			  {
				  Model.node=n;
				  return false;
			  }

			if(!AC())
			{
				if(level==0)
				{
					System.out.println("No");
		          	Model.node=n;
					return false;
				}
				
				for(int i=0;i<data.varN;i++)
				{
					vcur[i]=vcurlevel[level][i];
				}
				level--;
				
				int v=levelvdense[level];
				
				while(vcur[v]==0)
				{
					if(level==0)
					{
						System.out.println("No");
						Model.node=n;
						return false;
					}
					
					for(int i=0;i<data.varN;i++)
					{
						vcur[i]=vcurlevel[level][i];
					}
					level--;
					
					v=levelvdense[level];
				}
				
				  
		          int a=vdense[v][vcur[v]];//???????????????????????vdense[v][vcur[v]];??
                  
		          vsparse[v][a]=0;
                  vsparse[v][vdense[v][0]]=vcur[v];
                  vdense[v][vcur[v]]=vdense[v][0];
                  vdense[v][0]=a;
		          vcur[v]--;
		          
		          for(int i=0;i<vtocn[v];i++)
		          {
		        	  int c=vtoc[v][i];
		        	  assignedcount[c]--;
		          }
                  
		          addheap(v);
			}
			else
			{
				  n++;

				  double mindom=maxd+1;
				  int minvi=level;
				  int minv=levelvdense[level];
				  
				  for(int i=level;i<data.varN;i++)//data.varN
				  {
					   int v=levelvdense[i];
					   int ddeg=0;
					   
					   for(int j=0;j<vtocn[v];j++) //vtocN[v]
					   {
						   int cc=vtoc[v][j];//vtoc[v][j];
						   
						   /*boolean ff=false;
						   
						   for(int u=0;u<data.scopeN[cc];u++)//data.scopeN[cc]
						   {
							   if(vcur[data.scopes[cc][u]]!=0) //data.scopes[cc][u]
							   {
								   if(ff)
								   {
									   ddeg+=1;
									   break;
								   }
								   ff=true;
							   }
						   }*/
						   if(assignedcount[cc]+1<data.scopeN[cc])
						   {
							   ddeg+=1;							   
						   }
						   
					   }
					   
					   if(vcur[v]>0&&ddeg>0&&(1.0*(vcur[v]+1)/ddeg<mindom||(1.0*(vcur[v]+1)/ddeg==mindom&&v<minv)))
					   {
						   mindom=1.0*(vcur[v]+1)/ddeg;
						   minvi=i;
						   minv=v;
					   }
					   else if(mindom==maxd+1&&v<minv)
					   {
						   minvi=i;
						   minv=v;
					   }
				  }
				  
				  int a=levelvdense[level];
				  levelvdense[level]=levelvdense[minvi];
				  
				  levelvsparse[a]=minvi;
				  levelvsparse[levelvdense[minvi]]=level;
				  
				  levelvdense[minvi]=a;
				  
				  ///////////////////////////////////////////////////////////
		          int v=levelvdense[level];
		          
		          for(int i=0;i<vtocn[v];i++)
		          {
		        	  int c=vtoc[v][i];
		        	  assignedcount[c]++;
		          }
		          
		          level++;
		          if(level==data.varN)
		          {
		        	   /*System.out.println("yes");
		        	   for(int i=0;i<data.varN;i++)
		        	   {
		        		   int vv=levelvdense[i];
		        		   System.out.print("<"+"x"+vv+" "+vdense[vv][0]+">   ");
		        
		        	   }
		        	   
		        	   System.out.println("");
		           	   System.out.println("node: "+n);
		           	   */
		        	   Model.node=n;
		        	   return true;
		          }
		          
		          for(int i=0;i<data.varN;i++)
		          {
		        	  vcurlevel[level][i]=vcur[i];
		          }
		          
		          
                  if(vcur[v]>0)
		          {
       		    	  int val=maxd+1;
       		    	  int valn=0;
       		    	  for(int i=0;i<=vcur[v];i++)
       		    	  {
       		    		  if(vdense[v][i]<val)
       		    		  {
       		    			  valn=i;
       		    			  val=vdense[v][i];
       		    		  }
       		    	  }
       		    	  
       		    	  
    		          a=vdense[v][valn];
                      vdense[v][valn]=vdense[v][0];
                      vsparse[v][vdense[v][0]]=valn;
                      
                      vdense[v][0]=val;
       		    	  vsparse[v][val]=0;
                      
					  vcur[v]=0;
					  
					  addheap(v);
		          }
			  }
		          
		}
		Model.node=n;
		return false;
	}
	
	  boolean AC()
	  {
		  while(heapn!=0)
		  {

			  int x=gethead();
			  
			  for(int i=0;i<vtocn[x];i++)
			  {
				  int c=vtoc[x][i];

				  int y=data.scopes[c][0];
				  if(x==y)
				  {
					  y=data.scopes[c][1];
				  }
				  
				  if(revise(y,x))
				  {
					  if(vcur[y]<0)
					  {
						  for(int j=0;j<heapn;j++)
						  {
							  int v=heap[j];
							  inheap[v]=-1;
						  }
						  heapn=0;
						  return false;
					  }
					  
					  if(inheap[y]==-1)
					  {
						  addheap(y);
					  }
					  else
					  {
						  heapup(inheap[y]);
					  }
				  }
			  }			 			  
		  }
		  return true;
	  }
	
	boolean revise(int x,int y)
	{
		int n=vcur[x];
		boolean rel[][]=relMap[x][y];
		 for(int i=vcur[x];i>=0;i--)
		 {
			int a=vdense[x][i];
			
			int tt=seekSupport(rel,y,a);
			if(tt==-1)
			{
				vdense[x][i]=vdense[x][vcur[x]];
				vsparse[x][vdense[x][vcur[x]]]=i;

				vdense[x][vcur[x]]=a;
				vsparse[x][a]=vcur[x];
				
				vcur[x]--;
			}
		 }
		 return n!=vcur[x];
	}
	int seekSupport(boolean rel[][],int y,int a)
	{
		for(int j=0;j<=vcur[y];j++)
		{
			int b=vdense[y][j];
			if(rel[a][b])
			{
				return b;
			}
		}
		return -1;
	}
	
	void addheap(int v)
	{
		int p=heapn;
		int q=p;
		
		heapn++;
		
		while(p!=0)
		{
			p=(p-1)/2;
			int vv=heap[p];
			
			if(vcur[vv]<vcur[v])
			{
				break;
			}
			
			heap[q]=vv;
			inheap[vv]=q;

			q=p;
		}
		inheap[v]=q;
		heap[q]=v;
	}
	
	int gethead()
	{
		int a=heap[0];
		heapn--;
		
		heap[0]=heap[heapn];
		inheap[heap[heapn]]=0;
		heapdown(0);
		
		inheap[a]=-1;
		return a;
	}
	
	void heapup(int p)
	{
		int q=p;
		int v=heap[p];
		//System.out.println(y+" "+v);
		
		while(p>0)
		{
			p=(p-1)/2;
			int vv=heap[p];
			
			if(vcur[vv]<vcur[v])
			{
				break;
			}
			
			heap[q]=vv;
			inheap[vv]=q;
			q=p;
		}
		

		
		heap[q]=v;
		inheap[v]=q;
		
	}
	
	void heapdown(int p)
	{
		int v= heap[p];
	    int q=p;

		while(true)
		{
			q=p*2+1;
			
			if(q>heapn-1)
			{
				heap[p]=v;
				inheap[v]=p;
				break;
			}
			
			if(q==heapn-1)
			{
				
				int vv=heap[q];
				
				
				if(vcur[vv]>vcur[v])
				{
					heap[p]=v;
					inheap[v]=p;
					break;
				}
				
				heap[p]=vv;
				inheap[vv]=p;
				
				inheap[v]=q;
				heap[q]=v;
				break;
			}
			
			int vvv=heap[q+1];
			int vv=heap[q];
			
			if(vcur[vvv]<vcur[vv])
			{
				q=q+1;
				vv=vvv;
			}
			
			if(vcur[vv]>vcur[v])
			{
				heap[p]=v;
				inheap[v]=p;
				break;
			}
			
			heap[p]=vv;
			inheap[vv]=p;
			
			p=q;
		}
	}
}
