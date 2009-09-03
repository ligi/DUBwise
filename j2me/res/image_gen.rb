

def convert_problem(problem)
  p " problem #{problem}"
 exit false
end


system "cp -rv images/images_by_screensize/ images_by_screensize/"

resolutions=[{:width=>128,:height=>128},{:width=>176,:height=>220},{:width=>200,:height=>300},{:width=>240,:height=>320},{:width=>340,:height=>400},{:width=>480,:height=>640}]
resolutions.each { |r|
  
  act_path="images_by_screensize/#{r[:width]}x#{r[:height]}/"
  p "processing #{act_path}"

  case
  when r[:width]<200
    convert_problem "symbols" if !system "convert -verbose -geometry 110x45! images/symbols.png #{act_path}symbols.png" 
    convert_problem "icon" if !system "convert -verbose -geometry 24x24! images/icon.png #{act_path}i.png" 
    
  when r[:width]<250
    convert_problem "symbols" if !system "convert -verbose  -geometry 160x66! images/symbols.png #{act_path}/symbols.png"
    convert_problem "icon" if !system "convert -verbose -geometry 32x32! images/icon.png #{act_path}i.png" 

  when r[:width]<350
    convert_problem "symbols" if !system "convert -verbose  -geometry 160x66! images/symbols.png #{act_path}/symbols.png"
    convert_problem "icon" if !system "convert -verbose -geometry 64x64! images/icon.png #{act_path}i.png" 
    
  else
    convert_problem "symbols" if !system "convert -verbose  -geometry 320x132! images/symbols.png #{act_path}/symbols.png"
    convert_problem "icon" if !system "convert -verbose -geometry 96x96! images/icon.png #{act_path}i.png" 
    
  end
  
}
