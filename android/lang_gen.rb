#
# Script to generate the String Stuff for Android
#
# (cc) 2009-2010 by Marcus -ligi- Bueschleb 
#
#

require 'rubygems'
require 'rio'
require 'iconv'
require 'fileutils'

def escape(str2)
  str=str2
  str.gsub!(">","&gt;")
  str.gsub!("<","&lt;")
  str.gsub!("'","`")  # TODO FIXME - Dirty Hack - android R could not be build with a ' inside a str - broke fr lang ..
  return str
end

last_i=0

xmls={}
langs=["en","de","fr"]

#for each language
langs.each { |l|

  act_path="res/values-"+l

  #path sceme for default lang
  act_path="res/values"  if l=="en"

  act_fname=act_path+"/strings.xml"

  # create the path if not exist
  FileUtils.mkdir(act_path) if !File.exist?(act_path)

  # create the file if not existing
  File.new(act_fname,File::CREAT) if !File.exist?(act_fname)  

  # open rio handle
  xmls[l]=rio(act_fname)

  # initial content
  xmls[l] <  "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r<resources>\r"

  xmls[l] << rio("lang_all").read
}


#langdef=rio("shared_src/org/ligi/ufo/DUBwiseLangDefs.java")
langdef=rio("src/org/ligi/ufo/DUBwiseLangDefs.java")
langdef < "package org.ligi.ufo;\npublic interface DUBwiseLangDefs \n { \n"


to_rs=[]

Iconv.iconv( "UTF-8","ISO8859-1", rio("../j2me/res/lang_base").read).join.split("\n").each_with_index { |l,i|
 p "processing line:" + l
  splitted=l.split(";")
  langdef <<  " public final static int STRINGID_" + splitted.first+"="+i.to_s+";\n"

  # "case " + i.to_s + ": return R.string." + splitted.first                                                                                                                                                               
  to_rs<<"R.string."+splitted.first
  
  xmls["en"] << "\t<string name=\"" + splitted.first +  "\">"+escape(splitted[1])+"</string>\r"

  if splitted.length>2 && splitted[2]!=""
    xmls["de"] << "\t<string name=\"" + splitted.first +  "\">"+escape(splitted[2])+"</string>\r"
  else
    xmls["de"] << "\t<string name=\"" + splitted.first +  "\">"+escape(splitted[1])+"</string>\r"
  end
  
  if splitted.length>3 && splitted[3]!=""
    xmls["fr"] << "\t<string name=\"" + splitted.first +  "\">"+escape(splitted[3])+"</string>\r"
  else
    xmls["fr"] << "\t<string name=\"" + splitted.first +  "\">"+escape(splitted[1])+"</string>\r"
  end
  
  last_i=i                                                                                                                                                   
}                                                                                                                                                            

stringhelper=rio("src/org/ligi/android/dubwise_mk/helper/DUBwiseStringHelper.java")
stringhelper < ("package org.ligi.android.dubwise_mk.helper;\n import org.ligi.android.dubwise_mk.R;\npublic class DUBwiseStringHelper {\n  public final static int[] table= {" + to_rs.join(",") + "};}" )


langs.each { |l|
  xmls[l] << "</resources>"
  xmls[l].close 
}
  

langdef <<  " public final static int STRING_COUNT=" + (last_i+1).to_s+";"
langdef << "\n}\n"

