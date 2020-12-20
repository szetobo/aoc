(ns abagile.aoc.2020.day19
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [clojure.string :as cs]
    [instaparse.core :as insta]))

; (def input (->> (util/read-input-split "2020/day19.txt" #"\n\n")))
(def input1 (->> (util/read-input-split "2020/day19a.txt" #"\n\n")))
(def input2 (->> (util/read-input-split "2020/day19b.txt" #"\n\n")))

(def sample [["0: 4 1 5"
              "1: 2 3 | 3 2"
              "2: 4 4 | 5 5"
              "3: 4 5 | 5 4"
              "4: \"a\""
              "5: \"b\""]
             ["ababbb"
              "bababa"
              "abbbab"
              "aaabbb"
              "aaaabbb"]])

(comment
  (->> (first sample)
       (map (fn [s] (cs/replace s #"\d+" #(str \R %))))
       (map (fn [s] (cs/replace s #":" "=")))
       (cs/join "\n"))

  (def sample-parser (insta/parser "r0= r4 r1 r5
                                    r1= r2 r3 | r3 r2
                                    r2= r4 r4 | r5 r5
                                    r3= r4 r5 | r5 r4
                                    r4= \"a\"
                                    r5= \"b\""))
  (map sample-parser (second sample))

  (def rule-parser (insta/parser "rule = id <':'> (numbers | or-rule | char)
                                    id = number
                                    or-rule = numbers (<'|'> numbers)+
                                    char = (space quote #'[a-z]' quote space)
                                    <space> = <#'\\s*'>
                                    <quote> = <'\"'>
                                    numbers = number+
                                    number = (space #'[0-9]+' space)"))

  (def transform-opts {:number read-string})

  (->> (map rule-parser (first sample))
       (map (partial insta/transform transform-opts)))
  (->> (rule-parser "0: 130   90")
       (insta/transform transform-opts))
  (->> (rule-parser "1:130   90 | 20 30")
       (insta/transform transform-opts))
  (->> (rule-parser "8:\"a\"")
       (insta/transform transform-opts)))

(comment
  (->> (first sample)
       (map rule-parser))
  (->> (second sample)))

(defn part1 []
  (time (let [[rules msgs] input1
              msgs (cs/split-lines msgs)
              parser (insta/parser (-> rules (cs/replace #"\d+" #(str \R %))))]
          (->> (map parser msgs)
               (filter vector?)
               count))))

(defn part2 []
  (time (let [[rules msgs] input2
              msgs (cs/split-lines msgs)
              parser (insta/parser (-> rules (cs/replace #"\d+" #(str \R %))))]
          (->> (map parser msgs)
               (filter vector?)
               count))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))
