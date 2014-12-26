package parse;
import net.htmlparser.jericho.*;
import java.util.*;
import java.io.*;
import java.net.*;

import org.jsoup.Connection;
import org.jsoup.Jsoup;


public class ExtractText1 {
	
		
	public static void main(String[] args) throws Exception {
		class paper{
			String pdf=null;
			String pdfw=null;
			String html=null;
			String htmlw=null;
			String title=null;
			String authors=null;
			String Citations=null;
			int no_of_citations;
			int no_of_versions;
		}
		//Connection.Response html = Jsoup.connect("http://scholar.google.co.in/").execute();
		//System.out.println(html.body());
		String sourceUrlString="src/scholar.html";
		if (args.length==0)
		  System.err.println("Using default argument of \""+sourceUrlString+'"');
		else
			sourceUrlString=args[0];
		if (sourceUrlString.indexOf(':')==-1) sourceUrlString="file:"+sourceUrlString;
		MicrosoftConditionalCommentTagTypes.register();
		PHPTagTypes.register();
		PHPTagTypes.PHP_SHORT.deregister(); // remove PHP short tags for this example otherwise they override processing instructions
		MasonTagTypes.register();
		Source source=new Source(new URL(sourceUrlString));

		// Call fullSequentialParse manually as most of the source will be parsed.
		source.fullSequentialParse();

		System.out.println("Document title:");
		String title=getTitle(source);
		System.out.println(title==null ? "(none)" : title);

		System.out.println("\nDocument description:");
		String description=getMetaValue(source,"description");
		System.out.println(description==null ? "(none)" : description);

		System.out.println("\nDocument keywords:");
		String keywords=getMetaValue(source,"keywords");
		System.out.println(keywords==null ? "(none)" : keywords);
	
		System.out.println("\nLinks to other documents:");
		List<Element> linkElements=source.getAllElements(HTMLElementName.A);
		int i;int count=0;
		String label;
	
		for (i=0;i<linkElements.size();i++) {
			String href=linkElements.get(i).getAttributeValue("href");
			if (href==null) continue;
			// A element can contain other tags so need to extract the text from it:
			label=linkElements.get(i).getContent().getTextExtractor().toString();
			if(label.length()>=12&&label.substring(0,12).compareTo("Create alert")==0)
			count++;
			//System.out.println(label);
			//System.out.println(label+" <"+href+'>');
		   if(count>1)
			   break;
		}
		ArrayList<paper> find=new ArrayList<paper>();
		i++;
		;
		for(;i<linkElements.size();i++)
		{   label=linkElements.get(i).getContent().getTextExtractor().toString();
			if(label.length()>6&&label.substring(0,6).compareTo("Create")==0)
			break;
			paper temp=new paper();int s=i;int flag=0,flag1=0;String t=null,p = null,h=null,c=null,pw=null,hw=null;
			ArrayList<String> author = new ArrayList<>();
			for(;i<linkElements.size();i++)
		{
			
			label=linkElements.get(i).getContent().getTextExtractor().toString();
			if(flag==1)
			 {
				 
				 if(label.length()>5&&label.substring(0,5).compareTo("Cited")==0)
					 flag=0;
				 else
				 {   if(linkElements.get(i+1).getContent().getTextExtractor().toString().length()>5&&linkElements.get(i+1).getContent().getTextExtractor().toString().substring(0,5).compareTo("Cited")==0)
				   {  if(label.equalsIgnoreCase("i guyon"))
					   flag1=1;
					  //System.out.println(label+".");
					 author.add(label);
					
				   }
				 else
				 { if(label.equalsIgnoreCase("i guyon"))
					   flag1=1;
					 //System.out.print(label+",");
				 author.add(label);
				
				 }
				 }
			 }
		 if(s==i)
		 {   if(label.substring(0,5).compareTo("[PDF]")==0)
		 {
			// System.out.println(label+"\n");
			 if(linkElements.get(i+1).getContent().getTextExtractor().toString().length()>5&&linkElements.get(i+1).getContent().getTextExtractor().toString().substring(0,5).compareTo("Cited")!=0)
			 {
			 p=label;
			 pw=linkElements.get(i).getAttributeValue("href");
			 label=linkElements.get(++i).getContent().getTextExtractor().toString();
			 t=label;
			 //System.out.println("Title:"+label);
			 flag=1;
			// System.out.print("Authors:");
			 }
			 else
				 {
				 p=label;
				 flag=1;
			 }
		    }
		 else if(label.substring(0,6).compareTo("[HTML]")==0){
			  //System.out.println(label+"\n");
			 h=label;
			 hw=linkElements.get(i).getAttributeValue("href");
			 label=linkElements.get(++i).getContent().getTextExtractor().toString();
			//System.out.println("Title:"+label);
			 t=label;
			 flag=1;
			// System.out.print("Authors:");
		 }
			 else if(label.length()>5&&label.substring(0,5).compareTo("Cited")==0)
			 {
				  //System.out.println(label);	 
				 c=label; 
			 }
			 else if(label.substring(0,5).compareTo("[PDF]")!=0&&label.substring(0,6).compareTo("[HTML]")!=0)
			 {
				//System.out.println("Title:"+label);
				t=label;
				//flag=1;
				//System.out.print("Authors:");
			 }
		 
		 }
		
		 else if(s!=i)
		 {
			 if(label.length()>5&&label.substring(0,5).compareTo("[PDF]")==0)
			 {
				 // System.out.println(label+"\n");
				 p=label;
				 pw=linkElements.get(i).getAttributeValue("href");
				 label=linkElements.get(++i).getContent().getTextExtractor().toString();
				 t=label;
				 //System.out.println("Title:"+label);
				 flag=1;
				// System.out.print("Authors:");
			    }
			 else if(label.length()>6&&label.substring(0,6).compareTo("[HTML]")==0){
				  //System.out.println(label+"\n");
				 h=label;
				 hw=linkElements.get(i).getAttributeValue("href");
				 label=linkElements.get(++i).getContent().getTextExtractor().toString();
				 //System.out.println("Title:"+label);
				 t=label;
				 flag=1;
				// System.out.print("Authors:");
				 
			 }
		 }
			 
		 if(label.length()>5&&label.substring(0,5).compareTo("Cited")==0)
		 {
			  //System.out.println(label);	 
		 c=label;
		 }
		 if(label.length()>3&&label.substring(0,3).compareTo("All")==0){
			 String tu=label.substring(4);int k;
			 for(k=0;k<tu.length();k++)
			 {
				 if(Character.isDigit(tu.charAt(k)))
			     continue;
				 else
				break;	 
			 }
			 temp.no_of_versions=Integer.parseInt(tu.substring(0,k));
			 //System.out.println(temp.no_of_versions);
		 }
	   
		//System.out.println(label);
		 //System.out.println(temp.pdf);
		// System.out.println(temp.title);
		if(label.length()>=5&&label.substring(0,5).compareTo("Fewer")==0)
		{
	    System.out.print("");		
		break;
		}
		}
			if(flag1==1)
			{
				if(p!=null)
				{
			   // System.out.println(p);
				temp.pdf=p;
				temp.pdfw=pw;}
				if(h!=null)
				{
				//System.out.println(h);
				temp.html=h;
				temp.htmlw=hw;}
				//System.out.print("Title:"+t+"\n"+"Authors:");
				temp.title=t;
				temp.authors="Authors:";
				int j;
				for( j=0;j<author.size()-1;j++)
				{
				 //System.out.print(author.get(j)+",");
					temp.authors=temp.authors+author.get(j)+",";
				}
				//System.out.println(author.get(j)+".");
				temp.authors=temp.authors+author.get(j)+".";
				//System.out.println(c);
				temp.Citations=c;
			}
			else if(author.size()==0)
			{
			   if(p!=null){
				//System.out.println(p);
				temp.pdf=p;
				temp.pdfw=pw;}
	        	if(h!=null){
			    //System.out.println(h);
	        	temp.html=h;
	        	temp.htmlw=hw;}
	        	//System.out.println("Title:"+t);
	        	temp.title=t;
	        	//System.out.println(c);
	        	temp.Citations=c;
	        	temp.authors=null;
			}
			//System.out.println(temp.Citations);
			if(temp.Citations!=null)
			temp.no_of_citations=Integer.parseInt(temp.Citations.substring(9));
			//System.out.println(temp.no_of_citations);
			find.add(temp);
			
		}
		//System.out.println(find.size());
		//ArrayList<paper> fin=new ArrayList<paper>();
		for(i=0;i<find.size();i++)
		{   int b=find.get(i).no_of_citations;
		//System.out.println(b);
		     int pos=i;
		     int max=b;
			for(int j=i+1;j<find.size();j++)
			{   int c=find.get(j).no_of_citations;
			   //System.out.println(c);
				if(c>max)
				{max=c;
				pos=j;	
				}
			}
			Collections.swap(find,i,pos);
			if(find.get(i).Citations!=null){
			if(find.get(i).authors!=null&&find.get(i).title!=null)
			{
			if(find.get(i).pdf!=null){
			    System.out.println(find.get(i).pdf+" @ "+find.get(i).pdfw);}
		    if(find.get(i).html!=null){
		    	System.out.println(find.get(i).html+" @ "+find.get(i).htmlw);}
		    System.out.println("Title:"+find.get(i).title);
		    System.out.println(find.get(i).authors);
		    System.out.println(find.get(i).Citations);
		    System.out.println("No of versions:"+find.get(i).no_of_versions+"\n");
			}
			else if(find.get(i).authors==null&&find.get(i).title!=null)
			{
				if(find.get(i).pdf!=null)
				    System.out.println(find.get(i).pdf+" @ "+find.get(i).pdfw);
			    if(find.get(i).html!=null)
			    	System.out.println(find.get(i).html+" @ "+find.get(i).htmlw);
			    System.out.println("Title:"+find.get(i).title);
			    System.out.println(find.get(i).Citations);
			    System.out.println("No of versions:"+find.get(i).no_of_versions+"\n");
			}
			else if(find.get(i).title==null&&find.get(i).authors==null)
			{
				if(find.get(i).pdf!=null)
				    System.out.println(find.get(i).pdf+" @ "+find.get(i).pdfw);
			    if(find.get(i).html!=null)
			    	System.out.println(find.get(i).html+" @ "+find.get(i).htmlw);
			    System.out.println(find.get(i).Citations+"\n");
			    System.out.println("No of versions:"+find.get(i).no_of_versions+"\n");
			}
			}
		}
		

		//System.out.println("\nAll text from file (exluding content inside SCRIPT and STYLE elements):\n");
	//	System.out.println(source.getTextExtractor().setIncludeAttributes(true).toString());

		//System.out.println("\nSame again but this time extend the TextExtractor class to also exclude text from P elements and any elements with class=\"control\":\n");
		TextExtractor textExtractor=new TextExtractor(source) {
			public boolean excludeElement(StartTag startTag) {
				return startTag.getName()==HTMLElementName.P || "control".equalsIgnoreCase(startTag.getAttributeValue("class"));
			}
		};
		//System.out.println(textExtractor.setIncludeAttributes(true).toString());
  }

	private static String getTitle(Source source) {
		Element titleElement=source.getFirstElement(HTMLElementName.TITLE);
		if (titleElement==null) return null;
		// TITLE element never contains other tags so just decode it collapsing whitespace:
		return CharacterReference.decodeCollapseWhiteSpace(titleElement.getContent());
	}

	private static String getMetaValue(Source source, String key) {
		for (int pos=0; pos<source.length();) {
			StartTag startTag=source.getNextStartTag(pos,"name",key,false);
			if (startTag==null) return null;
			if (startTag.getName()==HTMLElementName.META)
				return startTag.getAttributeValue("content"); // Attribute values are automatically decoded
			pos=startTag.getEnd();
		}
		return null;
	}
}
