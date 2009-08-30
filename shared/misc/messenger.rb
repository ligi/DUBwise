# mailto: ligi atttt ligi dotttt de
require 'socket'
require 'fcntl'

class IPSocket
  def portInfo(out = STDOUT)
    out.puts "local:  #{addr[1]}\n" +
      "remote: #{peeraddr[1]}"
  end
end



if ARGV.length!=1 || !ARGV[0].include?(":")
  puts "USAGE:"
  puts "$> ruby messenger.rb [ip:port]"
  puts ""
  puts "using default address"
  host="0"
  port=9876
else
  host=ARGV[0].split(":").first
  port=ARGV[0].split(":").last.to_i  
end



puts "starting server at ip " + host + " port:" + port.to_s
serverSocket = TCPServer.new(host,port)

slots={ }
server = Thread.start {
  while (clientSocket = serverSocket.accept)
    Thread.start {
      clientSocket_= clientSocket
      clientSocket_.portInfo


      while true

      begin

        command=clientSocket_.gets
        puts command
        case
        when command =~ /new/
          clientSocket_.puts "new"
          if slots[command.delete("\r\n").split(":").last].nil?
            slots[command.delete("\r\n").split(":").last]={:from=>clientSocket_ , :to=>nil}
            break
          else
             clientSocket_.puts "slot exists"
          end
        when command =~ /conn/

          case
          when slots[command.delete("\r\n").split(":").last].nil?
            clientSocket_.puts "slot not found"
          when !slots[command.delete("\r\n").split(":").last][:to].nil?
            clientSocket_.puts "slot has partner"
          else
            clientSocket_.puts "conn"
            slots[command.delete("\r\n").split(":").last][:to]=clientSocket_

            break
          end

        when command =~ /all/
#          clientSocket_.puts slots.keys
          if slots=={}
              clientSocket_.puts "none"
          else
          slots.keys.each_with_index {|slot,i|
              clientSocket_.puts i.to_s + " | " + slot + ((slots[slot][:to].nil?)?"| free":"| connected")
            }
          end

          
        else
          clientSocket_.puts "Unknown command"

      end

        rescue =>e
        puts "err" + e
      end
      end
    }
end
}

while true
slots.each { |slot_name,hash|

#puts " processing " + slot_name


if !hash[:to].nil?
begin 

got=hash[:to].read_nonblock(3000)
puts slot_name +"<"+ got
hash[:from].write got
hash[:to].flush

rescue  Errno::EAGAIN => e
rescue =>e
 slots[slot_name][:to]=nil
  p e

end
end

begin

got=hash[:from].read_nonblock(3000)
puts slot_name +">"+ got
hash[:to].write got if !hash[:to].nil?
hash[:to].flush  if !hash[:to].nil?

rescue  Errno::EAGAIN => e
rescue =>e
hash[:to].close if !hash[:to].nil?
 slots.delete(slot_name) 
  p e

end

#
}
sleep 0.00005



end
