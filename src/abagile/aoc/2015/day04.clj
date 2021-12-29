(ns abagile.aoc.2015.day04
  (:gen-class)
  (:require
    [clojure.string :as cs]
    [clojure.test :refer [deftest is]])
  (:import
    [java.security MessageDigest]
    [java.math BigInteger]))

(def input "ckczppom")

(defn md5
  [s]
  (->> (.getBytes s)
       (.digest (MessageDigest/getInstance "MD5"))
       (BigInteger. 1)
       (format "%032x")))

(comment)

(defn part1
  []
  (time
    (loop [i 1]
       (if (cs/starts-with? (md5 (str input i)) "00000") i (recur (inc i))))))

(defn part2
  []
  (time
    (loop [i 117947]  ; starting from result of part1 + 1
       (if (cs/starts-with? (md5 (str input i)) "000000") i (recur (inc i))))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(comment
  (-main))

(deftest example
  (is (= true (cs/starts-with? (md5 "abcdef609043") "00000")))
  (is (= true (cs/starts-with? (md5 "pqrstuv1048970") "00000"))))
