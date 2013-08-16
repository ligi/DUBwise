require 'rubygems'
require 'rio'
require 'iconv'


debug=true

all_tabs=[]

all_names=[]
all_positions=[]
all_types=[]
all_namestarts=[]
all_lengths=[]
function_hash={}
all_categorys=[]
all_paramsids=[]

act_name=""
rio("definitions").read.each_line { |line|


if line =~ /--/
  act_name=line.split("--")[1].delete!("\r\n")
  function_hash[act_name]=""
else
  function_hash[act_name] << line
end
}

#puts function_hash.inspect
#exit

Dir["header_files/*.h"].sort.each { |e| 
 puts "----------------" + e if debug
  new="" 
  start=false 
  rio(e).each_line { |preLine| 
    ic = Iconv.new('UTF-8//IGNORE', 'UTF-8')
    l = ic.iconv(preLine + ' ')[0..-2]
    start=true if l =~ /struct\r/ 
    start=true if l =~ /struct\n/ 
    start=false if l =~ /\}/     
    if (l =~ /;/ ) && start
         new << l 
    end

  } 

act_pos=0
name_pos=0
end_pos=0



@categorys=[]
@category={}



def to_cat(name,items)
  @categorys << name
  @category[name]=[]   if !@category[name]
  @category[name] |= items
end


 new.each_line { |l|
 found=false
  function_hash.each_pair { |k,v|
    if l.scan(" "+k)!=[]
      found=true
      p "found" + k if debug
      puts v if debug
      puts act_pos if debug
   
      instance_eval(v)
      
    end
  }
  if !found
    p "Fatal: line not found " + l 
    p "stopping process"
    exit
  end
 }
#p @category
 @categorys.uniq!
 @categorys.sort!

@categorys.each { |e| 
  all_categorys << e;
}

mod_cats=@categorys.map {|e| "TABID_"+e } 
all_tabs << ["{" +  mod_cats.join(",") + "}"]
puts "[\"" +  @categorys.join("\",\"") + "\"]" if debug


all_names << ["{"+@categorys.map { |c|
                "{" + @category[c].map { |e|
                  "PARAMID_" + e[:function]
                }.join(",") + "}"
              }.join(",")+"}"]


@categorys.each { |c|
                @category[c].each { |e|
                   all_paramsids << e[:function]
                }
              }

all_positions << ["{"+@categorys.map { |c|
  "{" + @category[c].map { |e|
    e[:pos]
    }.join(",") + "}"
 }.join(",")+"}"]

all_types << ["{"+ @categorys.map { |c|
                "{PARAMTYPE_" + @category[c].map { |e|
    e[:typ]
                }.join(",PARAMTYPE_") + "}"
              }.join(",") + "}"]

all_namestarts << name_pos
all_lengths << end_pos
#rio(e+".new") <new 
 
} 

interface_name="MKParamsGeneratedDefinitions"
interface_path="../../../android/shared_src/org/ligi/ufo/"
interface_fname=interface_path+interface_name+".java"


interface_file=rio(interface_fname)

interface_file < "package org.ligi.ufo;\n"

interface_file << "public interface " + interface_name + " extends DUBwiseLangDefs\n"

interface_file << "{"+ "\n"

interface_file <<  "public final static int PARAMTYPE_BYTE=0;      // normal byte"+ "\n"
interface_file <<  "public final static int PARAMTYPE_MKBYTE=1;    // has potis @ end"+ "\n"
interface_file <<  "public final static int PARAMTYPE_BITSWITCH=2; // a bit aka boolean"+ "\n"
interface_file <<  "public final static int PARAMTYPE_STICK=3;     // a stick ( 1-12 )"+ "\n"
interface_file <<  "public final static int PARAMTYPE_KEY=4;       // a key"+ "\n"
interface_file <<  "public final static int PARAMTYPE_BITMASK=5;   // a bitmask ( byte )"+ "\n"
interface_file <<  "public final static int PARAMTYPE_CHOICE=6;"+ "\n\n\n"

all_categorys.uniq!
all_categorys.each_with_index { |e,i|
  interface_file <<  "public final static int TABID_"+e.to_s+"="+i.to_s+";\n";
}

interface_file <<  "\n\n"

all_paramsids.uniq!
all_paramsids.each_with_index { |e,i|
  interface_file <<  "public final static int PARAMID_"+e.to_s+"="+i.to_s+";\n";
}

interface_file <<  "// -- start generated code --"+ "\n"
interface_file <<  "public final static int[][] all_tab_stringids={"+all_tabs.join(",")+"};"+ "\n"
interface_file <<  "public final static int[][][] all_field_stringids={"+all_names.join(",")+"};"+ "\n"
interface_file <<  "public final static int[][][] all_field_positions={"+all_positions.join(",")+"};"+ "\n"
interface_file <<  "public final static int[][][] all_field_types={"+all_types.join(",")+"};"+ "\n"


interface_file <<  "public final static int[] all_name_positions={"+all_namestarts.join(",")+"};"+ "\n"
interface_file <<  "public final static int[] all_lengths={"+all_lengths.join(",")+"};"+ "\n"
interface_file <<  "// -- end generated code --"+ "\n"

interface_file << "}"+ "\n"

puts "written " + interface_fname


interface_name="MKParamsGeneratedDefinitionsToStrings"
interface_path="../../../android/shared_src/org/ligi/ufo/"
interface_fname=interface_path+interface_name+".java"


interface_file=rio(interface_fname)

interface_file < "package org.ligi.ufo;\n"

interface_file << "public interface " + interface_name + " extends DUBwiseLangDefs\n"

interface_file << "{"+ "\n"



interface_file <<  "public final static int[] TABID2STRINGID = { STRINGID_" + all_categorys.join(",STRINGID_") + "};\n"
interface_file <<  "public final static int[] PARAMID2STRINGID = { STRINGID_" + all_paramsids.join(",STRINGID_") + "};\n"

interface_file << "}"+ "\n"

puts "written " + interface_fname

