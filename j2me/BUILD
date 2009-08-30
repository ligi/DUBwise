#!/usr/bin/env ruby


####################################
#                                  #
### build-script for DUBwise #######
#                                  #
# Author: Marcus -LiGi- Bueschleb  #
#                                  #
# see README for further Infos     #
#                                  #
# e mail to ligi att ligi dott de  #
#                                  #
####################################


def p_usage
 puts "./BUILD [options] <profile> [bin_dir]"
 puts "options: 
      -z -> 7Zip bindir
      -l -> regenerate strings
      -i -> regenerate images
      -s -> regenerate sound
      -d -> debug output
      -o -> obfuscate"
end


@log_flag=false
def log str
  puts str if @log_flag
end


start_time=Time.now

resdir="res/"

version_split=File.read("version").delete("\n\r").split(".")
version_major=version_split[0]
version_minor=version_split[1]
version_patch=(version_split[2][0]-"a"[0]).to_s

p "building version #{version_major}.#{version_minor}#{version_split[2]}"
advzip_flags="-4"
if ARGV.include?("-f")
  advzip_flags=""
  ARGV.delete("-f")
end

zip_bindir= ARGV.include?("-z")
obfuscate=  ARGV.include?("-o")

@log_flag= ARGV.include?("-d")

ARGV.delete("-d")
ARGV.delete("-z")
ARGV.delete("-o")

if ARGV.include?("-l") | !File.exist?("res/langpacks")
  puts "regenerating language strings"
  p `cd res ; ruby lang_gen.rb ; cd ..`
  ARGV.delete("-l")
end

if ARGV.include?("-i")
  puts "regenerating images"
  p `cd res ; ruby image_gen.rb ; cd ..`
  ARGV.delete("-i")
end

if ARGV.include?("-s") | !File.exist?("res/voice_samples_by_name")
  puts "regenerating sounds"
  p `cd res ; ruby voice_gen.rb ; cd ..`
  ARGV.delete("-i")
end


bindir="bin"
set="test"

if ARGV.length==2
  bindir=ARGV[1]
  puts `rm -rf #{bindir}`
end


all_screensizes=["128x128","176x220","200x300","240x320","340x400","480x640"]
all_voice_modes=["no_voice","en_mp3_64kbit","en_mp3_32kbit","en_wav"]
all_bool_modes=["on","off"]
all_firmware_modes=["all_firmwares","no_firmwares","fc_mk3mag_firmwares"]
all_location_modes=["no_location","jsr179","openlapi"]
all_map_modes=["no_map","J2MEMap"]



screensizes=["240x320"]
voice_modes=["en_speedy"]
bluetooth_modes=["on"]
fileapi_modes=["on"]
devicecontrol_modes=["on"]
cldc11_modes=["on"]
firmware_modes=["all_firmwares"]
location_modes=["no_location"]
#map_modes=["no_map","J2MEMap"]
map_modes=["no_map"]


#defaults={{:screensizes=>["240x320"],:voice_moes=>["en_speedy"] , :bluetooth_modes=>["on"] , :devicecontrol_modes=>["on"],:fileapi_modes=>"off"
#"loeti"=>[{:screensizes=>["176x220"],:voice_moes=>["en_speedy"] , :bluetooth_modes=>["on"] , :devicecontrol_modes=>["on"] }  ] ,

 ARGV<<"" if ARGV==[]
case ARGV[0]

 when "K800"
  screensizes=["240x320"]
  voice_modes=["en_mp3_32kbit"]
  bluetooth_modes=["on"]
  devicecontrol_modes=["on"]
  fileapi_modes=["on"]
  cldc11_modes=["on"]
  firmware_modes=["all_firmwares"]
  location_modes=["no_location"]
  map_modes=["J2MEMap"]

 when "N80"
  screensizes=["340x400"]
  voice_modes=["en_mp3_32kbit"]
  bluetooth_modes=["on"]
  devicecontrol_modes=["on"]
  fileapi_modes=["on"]
  cldc11_modes=["on"]
  firmware_modes=["no_firmwares"]
  location_modes=["no_location"]
  map_modes=["J2MEMap"]

 when "wtk"
  screensizes=["240x320"]
  voice_modes=["en_wav_orig"]
  bluetooth_modes=["on"]
  devicecontrol_modes=["off"]
  fileapi_modes=["on"]
  cldc11_modes=["on"]
  firmware_modes=["all_firmwares"]
  location_modes=["no_location"]
  map_modes=["J2MEMap"]

 when "off"
  screensizes=["240x320"]
  voice_modes=["no_voice"]
  bluetooth_modes=["off"]
  devicecontrol_modes=["off"]
  fileapi_modes=["off"]
  cldc11_modes=["off"]


 when "all"
  screensizes=all_screensizes
  voice_modes=all_voice_modes
  bluetooth_modes=all_bool_modes
  fileapi_modes=all_bool_modes
  devicecontrol_modes=all_bool_modes
  cldc11_modes=all_bool_modes
  firmware_modes=all_firmware_modes
  location_modes=all_location_modes
  map_modes= all_map_modes


 when "allwm"
  screensizes=["240x320","340x400","480x640"]
  voice_modes=["no_voice"]
  bluetooth_modes=["on"]
  fileapi_modes=["off"]
  devicecontrol_modes=["off"]
  cldc11_modes=["on"]
  firmware_modes=["no_firmwares"]
  location_modes=["no_location"]
  map_modes=["J2MEMap"]

 when "alloff"
  screensizes=all_screensizes
  voice_modes=all_voice_modes

  bluetooth_modes=["off"]
  fileapi_modes=["off"]
  devicecontrol_modes=["off"]
  cldc11_modes=["off"]
  firmware_modes=all_firmware_modes
  location_modes=["no_location"]

 else

  puts "no valid profile given"
  exit



end


puts `mv build.props build.props.bak`
puts `ant clean`
puts `mkdir #{bindir}`

propertys={}
 allthreads=[]

build_count= map_modes.length*location_modes.length*  firmware_modes.length*   cldc11_modes.length*      devicecontrol_modes.length*fileapi_modes.length* bluetooth_modes.length* screensizes.length* voice_modes.length
                
puts "build_count: " + build_count.to_s
sleep 1


build=0
last_time=Time.now
new_build=true
reprocess_lib=true
puts "cleaning ressources"
p `rm -rf tmp/res `
p `mkdir tmp/res `
p `cp -v res/langpacks/* tmp/res/`

cldc11_modes.each { |cldc11|
  reprocess_lib=true
  
  ### modifiers from here wont affect the communicator-lib
  map_modes.each { |map_mode|
  location_modes.each {|location_mode|
    devicecontrol_modes.each { |devicecontrol|
      fileapi_modes.each { |fileapi|
        bluetooth_modes.each { |bluetooth|

          new_build=true
          ### modifiers from here wont affect source - just ressources
          voice_modes.each { |voice_mode|
            new_voice=true
            

           screensizes.each { |screensize|
              new_screensize=true
              
              firmware_modes.each {|firmware|
                #                new_firmwares=true                
                build=build+1
                last_time=Time.now
                
                propertys["screensize"]=screensize


#                if allthreads.length>7

#                  allthreads.each { |t|
#                  p "waiting for thread" + t.inspect
#                    t.join
#                  }
#                  allthreads=[]                
#                end
                
                propertys["voice_mode"]=voice_mode
                propertys["bluetooth"]=bluetooth
                propertys["fileapi"]=fileapi
                propertys["devicecontrol"]=devicecontrol
                propertys["cldc11"]=cldc11

                  if cldc11=="on"
                    propertys["cldc"]="1.1"
                  else
                    propertys["cldc"]="1.0"
                  end
                propertys["version_major"]=version_major
                propertys["version_minor"]=version_minor
                propertys["version_patch"]=version_patch
                
                
                propertys["basename"]="DUBwise-#{screensize}-#{voice_mode}-#{firmware}"
                
                if  location_mode=="jsr179" 
                  propertys["basename"]+="-LocationAPI"
                end

                if  location_mode=="openlapi" 
                  propertys["basename"]+="-OpenLAPI"
                end
                
                if location_mode=="jsr179" 
                  propertys["jsr179"]="on"
                else
                  propertys["jsr179"]="off"
                end


                if location_mode=="openlapi" 
                  propertys["openlapi"]="on"
                else
                  propertys["openlapi"]="off"
                end

                if ((location_mode=="jsr179" ) || (location_mode=="openlapi" ))
                  propertys["location"]="on"
                else
                  propertys["location"]="off"
                end


     
                
                propertys["basename"]+="-CLDC11"    if  cldc11=="on"
                propertys["basename"]+="-FileAPI"    if  fileapi=="on"
                propertys["basename"]+="-BluetoothAPI"    if  bluetooth=="on"
                propertys["basename"]+="-DeviceControl"    if  devicecontrol=="on"
                propertys["basename"]+="-J2MEMap"   if map_mode=="J2MEMap"

                propertys["bin_path"]="#{screensize}/#{cldc11=="on"?"cldc11":"cldc10"}/#{voice_mode}/bluetooth_#{bluetooth}/fileapi_#{fileapi}/devicecontrol_#{devicecontrol}/#{location_mode}"

                p "---------------"
                p    "processing: "
                p    "voice_mode" + voice_mode
                p    "Screensize " + screensize                


                if new_build

                  puts "compiling"
                  prefs_file= File.new("build.props","w")
                  propertys.each_pair {|name,value|
                    prefs_file <<	name+"="+value+"\n"
                  }
#                  prefs_file <<	"libs=lib/J2meMap_lib_cldc11.jar:lib/openlapi-jsr179-0.9.11.jar:lib/openlapi-0.9.11.jar\n"
#                  prefs_file << "classes=lib/classes_with_fileapi_and_jsr179.zip\n"
#                  prefs_file << "classes=lib/classes_with_fileapi.zip\n"

                  prefs_file.close

                  if reprocess_lib
                    puts `ant preprocess_lib`
                    reprocess_lib=false
                  end
                  
                  build_params=""
                  build_params+=" -Dobfuscate=true" if obfuscate
                  build_params+=" -Dobfuscate=false" if !obfuscate


                  build_params+=" -Dlibs="
                  build_params+="lib/J2meMap_lib_cldc11.jar" if map_mode=="J2MEMap"
#                  build_params+=":lib/openlapi-jsr179-0.9.11.jar:lib/openlapi-0.9.11.jar"  if  location_mode=="openlapi" 

                  build_params+=":lib/openlapi-0.9.11.jar"  if  location_mode=="openlapi" 
                  build_params+=":lib/xml.jar"  if  location_mode=="openlapi" ||  map_mode=="J2MEMap"


                  if  location_mode=="jsr179" 
                    build_params+=" -Djsr179=on"
                    build_params+=" -Dclasses=lib/classes_with_fileapi_and_jsr179.zip"
                  else
                    build_params+=" -Dclasses=lib/classes_with_fileapi.zip"
                    build_params+=" -Djsr179=off"
                  end

                  if map_mode=="J2MEMap"
                     build_params+=" -Dj2memap=on"
                  else
                     build_params+=" -Dj2memap=off"
                  end


#                  p propertys.inspect
#                  `ant #{build_params} build`.each_line {	
#                    |l| puts l 
#                    exit if  (l =~ /\[wtkbuild\].*error/)
#                  }

                  exit if !(system "ant #{build_params} build")

                  new_build=false  
                  
                  `rm -rf tmp/bin`
                  `mkdir -p tmp/bin`

                  
#                  `mv build/bin/#{propertys["basename"]}.jar tmp/bin`
                  `mv build/bin/#{propertys["basename"]}.jar tmp/bin/tmp.jar`
#                  `advzip -z -4 #{advzip_flags} tmp/bin/tmp.jar`

                  `rm build/bin/#{propertys["basename"]}.jad`
                  
#                  `cd tmp/bin ; unzip #{propertys["basename"]}.jar ; rm #{propertys["basename"]}.jar ; cd ../..`
                  
                  #                else
                  #                  `ant only_jar`.each_line {	
                  #                    |l| puts l 
                  #                    exit if  (l =~ /\[wtkbuild\].*error/)
                  
                  #                  }
                  
                end # new build
                

                
                #                if new_firmwares
###                p `rm -rf tmp/bin/*.bin `
###                p `cp -v res/firmwares/#{firmware}/* tmp/bin`

                p `cp -v tmp/bin/tmp.jar #{bindir}/#{propertys["basename"]}.jar`
#exit
#                p `advzip #{advzip_flags} -a  #{bindir}/#{propertys["basename"]}.jar  res/images_by_screensize/#{screensize}/*`

#                p `advzip  #{advzip_flags} -a  #{bindir}/#{propertys["basename"]}.jar   res/voice_samples_by_name/#{voice_mode}/*`

#                p `advzip  #{advzip_flags} -a  #{bindir}/#{propertys["basename"]}.jar    res/firmwares/#{firmware}/*`

#                p `advzip #{advzip_flags} -u  #{bindir}/#{propertys["basename"]}.jar  res/images_by_screensize/#{screensize}/* res/voice_samples_by_name/#{voice_mode}/*  res/firmwares/#{firmware}/*`


#                p `advzip #{advzip_flags} -u  #{bindir}/#{propertys["basename"]}.jar  res/images_by_screensize/#{screensize}/* res/voice_samples_by_name/#{voice_mode}/*  res/firmwares/#{firmware}/*`

#

 #               p `7z #{advzip_flags} a  -x.svn* #{bindir}/#{propertys["basename"]}.jar  res/images_by_screensize/#{screensize}/* res/voice_samples_by_name/#{voice_mode}/*  res/firmwares/#{firmware}/*`


#                p `7z #{advzip_flags} a  -x.svn* #{bindir}/#{propertys["basename"]}.jar  res/images_by_screensize/#{screensize}/* res/voice_samples_by_name/#{voice_mode}/*  res/firmwares/#{firmware}/*`
  
#               allthreads<< Thread.new 
                #{
                


                  @basename=propertys["basename"]
#                  ` mkdir tmp/#{@basename} `
                  @screensize=screensize
                  @firmware=firmware
                  @voice_mode=voice_mode



              log "compressing images"
                  log `cd res/images_by_screensize/#{@screensize} ; 7z a -x\!.svn* -w../../../tmp/ ../../../#{bindir}/#{@basename}.jar * ; cd ../../.. ` #  res/images_by_screensize/#{screensize}/* res/voice_samples_by_name/#{voice_mode}/*  res/firmwares/#{firmware}/*`
                  

                log "compressing firmwares"
                  log `cd res/firmwares/#{@firmware} && 7z a  -x\!.svn* -w../../../tmp/ ../../../#{bindir}/#{@basename}.jar * && cd -`
                  
                log "compressing sounds"
                  log `cd res/voice_samples_by_name/#{@voice_mode} ; 7z a -x\!.svn* -w../../../tmp/ ../../../#{bindir}/#{@basename}.jar * ; cd ../../..`


  #              log "compressing images"
#                  log `cd res/images_by_screensize/#{@screensize} ; 7z a -w../../../tmp/#{@basename} -x\!.svn* ../../../#{bindir}/#{@basename}.jar * ; cd ../../.. ` #  res/images_by_screensize/#{screensize}/* res/voice_samples_by_name/#{voice_mode}/*  res/firmwares/#{firmware}/*`
                  

 #               log "compressing firmwares"
  #                log `cd res/firmwares/#{@firmware} ; 7z a  -x\!.svn*  -w../../../tmp/#{@basename} ../../../#{bindir}/#{@basename}.jar * ; cd ../../..`
                  
   #             log "compressing sounds"
  #                log `cd res/voice_samples_by_name/#{@voice_mode} ; 7z a -x\!.svn* -w../../../tmp/#{@basename}/ ../../../#{bindir}/#{@basename}.jar * ; cd ../../..`


                #                end
#                   new_screensize
###                if new_screensize
###                  p `rm -rf tmp/bin/*.png  `
###                  p `rm -rf tmp/bin/*.jpg  `                
###                  p `cp -v res/images_by_screensize/#{screensize}/* tmp/bin/`
###                  new_screensize=false
###                end
###                if new_voice
###                  p `rm tmp/bin/*.wav `
###                  p `rm  tmp/bin/*.mp3 `
###                  p `cp -v res/voice_samples_by_name/#{voice_mode}/* tmp/bin/`  
###                  new_voice=false
###                end
#                p `advzip -4 -a #{bindir}/#{propertys["basename"]}.jar  tmp/bin/`

# normal zip is larger than advzip and not working on e.g. N80 ( took me som time to figure out)       p `cd tmp/bin ; zip  ../../#{bindir}/#{propertys["basename"]}.jar  -r . ; cd ../..`

   #             p `cd tmp ; advzip -4 -a  ../#{bindir}/#{propertys["basename"]}.jar   bin/* ; cd ..`

                #                `cp tmp/jad/*.jad #{bindir}/#{propertys["basename"]}.jad`
                
 #                 puts "#{bindir} ----+ " + Dir.pwd
                jarsize=File.size("#{bindir}/#{@basename}.jar").to_s
                jad_file=File.new("#{bindir}/#{@basename}.jad","w+")
                
                jad_file << "MIDlet-Jar-URL: #{@basename}.jar\n"
                jad_file << "MIDlet-Jar-Size: #{jarsize}\n"
                jad_file << "MIDlet-Name: DUBwise\n"
                jad_file << "MIDlet-Vendor: LiGi\n"
                jad_file << "MIDlet-Version: " + version_split[0..1].join(".") + "\n"
                jad_file << "MIDlet-1: DUBwise, i.png, DUBwise\n"
                jad_file << "MIDletX-No-Command: true\n"
                jad_file << "MIDlet-Icon: i.png\n"
#                jad_file << "MIDlet-Install-Notify: http://ligi.selfip.org/ligi/mail?subject=DUBWiseInstallNotify&text=jarname_#{@basename}\n"
#                jad_file << "MIDlet-Delete-Notify: http://ligi.selfip.org/ligi/mail?subject=DUBWiseDeleteNotify&text=jarname_#{@basename}\n"

                jad_file.close


                puts "done build # " + build.to_s  + "/" + build_count.to_s + " Time:" + (Time.now-last_time ).to_s + " overall: #{((Time.now-start_time)/60).to_i.to_s}:#{((Time.now-start_time)%60).to_i.to_s}"
                p    "voice_mode" + voice_mode
                p    "Screensize " + screensize
                p    "size" + jarsize.to_s
                p "---------------"
                  #                }
                #                `mkdir -p #{bindir}/#{propertys["bin_path"]}`
                
                #          `mv build/bin/#{propertys["basename"]}* #{bindir}/#{propertys["bin_path"]}`
                
                ##`mkdir -p #{bindir}/#{propertys["bin_path"]}`
                
                ##`mv build/bin/#{propertys["basename"]}* #{bindir}/#{propertys["bin_path"]}`


                ###`mkdir -p #{bindir}/`
                
                ###`mv build/bin/#{propertys["basename"]}* #{bindir}/`


#                `mkdir -p #{bindir}/`
                
#                `mv build/bin/#{propertys["basename"]}* #{bindir}/`
                
              }
                

              
              }
              new_build=true if voice_mode=="no_voice"              
          }
        }
      }
    }
  }
}
}

sleep 3
puts "Joining all open threads (" + allthreads.length.to_s + "open)" + `pwd`
#sleep allthreads.length
allthreads.each { |t|
  t.join
}
allthreads=[]

puts `mv -v  build.props.bak build.props`

puts "Finished in #{((Time.now-start_time)/60).to_i.to_s}:#{((Time.now-start_time)%60).to_i.to_s}"



if zip_bindir
 puts "Zipping bindir"
  if File.exist?( "#{bindir}.7z")
    puts "Zip allready there"
  else
    `7z a #{bindir}.7z #{bindir}`
  end
end


