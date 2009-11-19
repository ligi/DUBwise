require 'rubygems'
require 'rio'

last_i=0
puts `rm -rfv langpacks`
puts `mkdir langpacks`
langpack=rio("langpacks/l") 

langdef=rio("../../shared/src/org/ligi/ufo/DUBwiseLangDefs.java") 

langdef < "package org.ligi.ufo;\npublic interface DUBwiseLangDefs \n { \n"

rio("lang_base").read.split("\n").each_with_index { |l,i|
  langdef << " public final static int STRINGID_" + l.split(";").first+"="+i.to_s+";\n"
  
  l=l.split(";")[1..-1].join(";")+"\n"
  langpack<<l

  last_i=i
}

langdef <<  " public final static int STRING_COUNT=" + (last_i+1).to_s+";"
langdef << "\n}\n"
