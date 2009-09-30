require 'rio'


debug=false

all_tabs=[]

all_names=[]
all_positions=[]
all_types=[]

all_namestarts=[]
all_lengths=[]

function_hash={}
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
  rio(e).each_line { |l| 
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
mod_cats=@categorys.map {|e| "STRINGID_"+e } 
all_tabs << ["{" +  mod_cats.join(",") + "}"]
puts "[\"" +  @categorys.join("\",\"") + "\"]" if debug


all_names << ["{"+@categorys.map { |c|
                "{" + @category[c].map { |e|
    "STRINGID_" + e[:function]
                }.join(",") + "}"
              }.join(",")+"}"]

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

puts "// -- start generated code --"
puts "public final static int[][] all_tab_stringids={"+all_tabs.join(",")+"};"
puts "public final static int[][][] all_field_stringids={"+all_names.join(",")+"};"
puts "public final static int[][][] all_field_positions={"+all_positions.join(",")+"};"
puts "public final static int[][][] all_field_types={"+all_types.join(",")+"};"


puts "public final static int[] all_name_positions={"+all_namestarts.join(",")+"};"
puts "public final static int[] all_lengths={"+all_lengths.join(",")+"};"
puts "// -- end generated code --"
