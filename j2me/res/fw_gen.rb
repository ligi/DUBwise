#require 'rubygems'
require 'rio'

system "mkdir tmp ; cd tmp ; mkdir firmwares ; cd firmwares ; svn co http://mikrokopter.de/mikrosvn/FlightCtrl/tags fc ; svn co http://mikrokopter.de/mikrosvn/NaviCtrl/tags nc"

exit

def hex2bin ( source_file , dest_file )
  
  plain_str=""
  last_addr=-1
  line_id=0;
  lines=0
  block=false
  File.read(source_file).each_line {|l|
    lines=lines+1
  }
  File.read(source_file).each_line {|l|
    line_id=line_id+1
    l.delete!("\n\r") # just to make sure
    data= l[9,(l.length-11)]
    size=l[1,2].to_i(16)
    addr=l[3,4].to_i(16)
    rectype=l[7,2].to_i(16)
    if rectype!=0
      p "rectype " + rectype.to_s
    end
    if ((last_addr!=-1)&&(addr!=last_addr))
      block=true
#      p "addr invalid" + addr.to_s(16)
#      p "data" + data
      p "line " +line_id.to_s +  "/" + lines.to_s + "=" + l
#      exit
    end
    last_addr=addr+size
    if size!= data.length/2
      p "data size not matching" + size +"vs"+ data.length.to_s
      exit
    end


    plain_str<<data if (rectype!=2)
    if rectype==2
     p "dropping rectype2"
    end
#    puts ">"+ l[9,(l.length-11)]
  }
  
  
  puts "size:" + (plain_str.length/2.0).to_s
  
  #return
  new = []
  (plain_str.length/2).times {|i|
    new << (plain_str[(i*2)..(i*2+1)].to_i(16))
  }
  
  
  foo=rio( dest_file)
  foo.write( ((new.length >> 24 )&0xff).chr)
  foo.write( ((new.length >> 16 )&0xff).chr)
  foo.write( ((new.length >> 8 )&0xff).chr)
  foo.write( ((new.length&0xff)).chr)
  new.each { |c|
    foo.write( c.chr)
  }
end

fc_lst=[]
nc_lst=[]

`rm firmwares/ -rf`
`mkdir -p firmwares/fc_mk3mag_firmwares`
`mkdir firmwares/all_firmwares`
`mkdir firmwares/no_firmwares`

Dir["orig/firmwares/public/*"].each { |hex_file|
  file_base=hex_file.split("/").last.gsub!(".hex","")
  p "processing: " +hex_file
  case hex_file
  when  /Navi/
    version=file_base.gsub("Navi-Ctrl","").gsub("_STR9","").gsub("_V","").gsub("_",".")
    p "navi " + version
    hex2bin(hex_file,"firmwares/all_firmwares/fw_224_" + nc_lst.length.to_s+".bin")
    nc_lst<<version
  when /Flight-Ctrl/
    version=file_base.gsub("Flight-Ctrl","").gsub("_MEGA644","").gsub("_V","").gsub("_",".")
    p "fc" + version

    hex2bin(hex_file,"firmwares/all_firmwares/fw_116_" + fc_lst.length.to_s+".bin")
    hex2bin(hex_file,"firmwares/fc_mk3mag_firmwares/fw_116_" + nc_lst.length.to_s+".bin")

    fc_lst<<version
    #    fc_fw_count=fc_fw_count+1
  else
    puts "Hex not recognized"
  end
}



rio("firmwares/fc_mk3mag_firmwares/fw_116.lst") < fc_lst.join("\n")
rio("firmwares/fc_mk3mag_firmwares/fw_116.lst") << "\n"

rio("firmwares/all_firmwares/fw_116.lst") < fc_lst.join("\n")
rio("firmwares/all_firmwares/fw_116.lst") << "\n"



rio("firmwares/all_firmwares/fw_224.lst") < nc_lst.join("\n")
rio("firmwares/all_firmwares/fw_224.lst") << "\n"



