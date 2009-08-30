
Dir["*.hex"].each { |f|

case
when f=~ /Navi/
  `ruby hex2bin.rb #{f}  /home/ligi/prj/DUBwise/j2me/res/firmwares/all_firmwares/navi.bin`

p "written nc"


when f =~ /Flight/
  `ruby hex2bin.rb #{f}  /home/ligi/prj/DUBwise/j2me/res/firmwares/all_firmwares/fc.bin`
p "written fc"

when f =~ /MK3Ma/
  `ruby hex2bin.rb #{f}  /home/ligi/prj/DUBwise/j2me/res/firmwares/all_firmwares/mk3.bin`
p "written mk3"

else
p "cant handle" + f

end

}
