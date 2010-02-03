#
# Script to generate the String Stuff for Android
#

require 'rubygems'
require 'rio'
require 'iconv'

last_i=0
#puts `rm -rfv langpacks`
#puts `mkdir langpacks`
#langpack=rio("langpacks/l")

xml_en=rio("res/values-en/strings.xml")
xml_de=rio("res/values-de/strings.xml")
xml_fr=rio("res/values-fr/strings.xml")

xml_en < "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r<resources>"
xml_de < "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r<resources>"
xml_fr < "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r<resources>"


langdef=rio("shared_src/org/ligi/ufo/DUBwiseLangDefs.java")

langdef < "package org.ligi.ufo;\npublic interface DUBwiseLangDefs \n { \n"


to_rs=[]


 Iconv.iconv( "UTF-8","ISO8859-1", rio("../j2me/res/lang_base").read).join.split("\n").each_with_index { |l,i|
splitted=l.split(";")
  langdef <<  " public final static int STRINGID_" + splitted.first+"="+i.to_s+";\n"

  # "case " + i.to_s + ": return R.string." + splitted.first                                                                                                                                                               
  
  
  to_rs<<"R.string."+splitted.first
  
  xml_en << "\t<string name=\"" + splitted.first +  "\">"+splitted[1]+"</string>\r"

  if splitted.length>2
    xml_de << "\t<string name=\"" + splitted.first +  "\">"+splitted[1]+"</string>\r"
  else
    xml_de << "\t<string name=\"" + splitted.first +  "\">"+splitted[1]+"</string>\r"
  end
  
  if splitted.length>3
    xml_fr << "\t<string name=\"" + splitted.first +  "\">"+splitted[1]+"</string>\r"
  else
    xml_fr << "\t<string name=\"" + splitted.first +  "\">"+splitted[1]+"</string>\r"
  end
  
  #langpack<<l
  
  last_i=i                                                                                                                                                   
}                                                                                                                                                            

p to_rs.join(",")
xml_en << "</resources>"
xml_de << "</resources>"
xml_fr << "</resources>"
                                                                                                                                                             

langdef <<  " public final static int STRING_COUNT=" + (last_i+1).to_s+";"
langdef << "\n}\n"
