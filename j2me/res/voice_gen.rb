
p `rm -rfv voice_samples_by_name`
p `mkdir voice_samples_by_name`

Dir["voices/*"].each { |voice|

voice_clean=voice.split("/").last

  p "voice_set: " + voice_clean


  p `mkdir voice_samples_by_name/#{voice_clean}_wav`


  Dir[ voice+"/*"].each { |sample|
    p sample
    sample_clean=sample.split("/").last.split(".").first
    p `ffmpeg -i #{sample} -ar 8000 voice_samples_by_name/#{voice_clean}_wav/#{sample_clean}.wav`
  }

  p `rm -rfv voice_samples_by_name/#{voice_clean}_wav_orig`
  p `mkdir voice_samples_by_name/#{voice_clean}_wav_orig`


  Dir[ voice+"/*"].each { |sample|
    p sample
    sample_clean=sample.split("/").last.split(".").first
    p `cp #{sample} voice_samples_by_name/#{voice_clean}_wav_orig/#{sample_clean}.wav`
  }


  p `mkdir voice_samples_by_name/#{voice_clean}_mp3_16kbit`


  Dir[ voice+"/*"].each { |sample|
    p sample
    sample_clean=sample.split("/").last.split(".").first
    p `lame #{sample} -b 16 voice_samples_by_name/#{voice_clean}_mp3_16kbit/#{sample_clean}.mp3`
  }


  p `mkdir voice_samples_by_name/#{voice_clean}_mp3_32kbit`


  Dir[ voice+"/*"].each { |sample|
    p sample
    sample_clean=sample.split("/").last.split(".").first
    p `lame #{sample} -b 32 voice_samples_by_name/#{voice_clean}_mp3_32kbit/#{sample_clean}.mp3`
  }


  p `mkdir voice_samples_by_name/#{voice_clean}_mp3_64kbit`


  Dir[ voice+"/*"].each { |sample|
    p sample
    sample_clean=sample.split("/").last.split(".").first
    p `lame #{sample} -b 64 voice_samples_by_name/#{voice_clean}_mp3_64kbit/#{sample_clean}.mp3`
  }

}
