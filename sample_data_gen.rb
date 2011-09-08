require 'rubygems'
require 'json'
denominations = [1,5,10,25,50]

prices = (1..99).to_a

exchanges = prices.map do |price|
  neg_price = 100-price
  pos_path = denominations.reverse.map{ |coin_size|
    result = price/coin_size
    price = price % coin_size
    result
  }
  pos_exch_num = pos_path.inject(0){|sum, n| sum + n.abs }

  neg_path = denominations.reverse.map{ |coin_size|
    result = neg_price/coin_size
    neg_price = neg_price % coin_size
    result * -1
  }
  neg_exch_num = neg_path.inject(0){|sum, n| sum + n.abs }

  exch_num = nil; path = nil;

  if neg_exch_num < pos_exch_num
    path = neg_path; exch_num = neg_exch_num
  else
    path = pos_path; exch_num = pos_exch_num
  end

  [exch_num, path.reverse]
end

data = {"denominations" => denominations, "exchanges" => exchanges}
require 'pp'
File.open("sample_data.json", 'w') do |f|
  f.puts data.to_json
end
