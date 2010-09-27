#!/usr/bin/ruby

require 'inifile'

mkms={}

Dir["mixer_definitions/*.mkm"].each { |file|
  ini=IniFile.new(file)

  res=""
                
  (1..12).each { |engine_id|
    res+=ini["Gas"]["Motor"+engine_id.to_s].to_s+","
    res+=ini["Nick"]["Motor"+engine_id.to_s].to_s+","
    res+=ini["Roll"]["Motor"+engine_id.to_s].to_s+","
    res+=ini["Yaw"]["Motor"+engine_id.to_s].to_s

    res+="," if engine_id!=12
  }
  mkms[file.gsub(".mkm","").gsub("mixer_definitions/","")]=res
}


puts "public final static String[] default_names={\"" + mkms.keys.sort.join("\",\"") + "\"};"
puts "public final static byte[][] default_arrays={{" + mkms.keys.sort.map{ |e| mkms[e] }.join("},{") + "}};"

