require 'rio'

plain_str=""
rio(ARGV[0]).read.each_line {|l|
#  plain_str << l[11..-3].delete("\n\r")

puts l
l.delete!("\n\r")
plain_str<< l[9,(l.length-11)]
puts ">"+ l[9,(l.length-11)]
}


puts plain_str.length/2.0

#return
new = []
(plain_str.length/2).times {|i|
  new << (plain_str[(i*2)..(i*2+1)].to_i(16))
}


foo=rio(ARGV[1])
foo.write( ((new.length >> 24 )&0xff).chr)
foo.write( ((new.length >> 16 )&0xff).chr)
foo.write( ((new.length >> 8 )&0xff).chr)
foo.write( ((new.length&0xff)).chr)
new.each { |c|
  foo.write( c.chr)
}

puts new.length



