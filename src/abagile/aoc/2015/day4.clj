(ns abagile.aoc.2015.day4
  (:gen-class)
  (:require
    [clojure.string :as cs])
  (:import
    [java.security MessageDigest]
    [java.math BigInteger]))

(defn md5 [s]
  (->> s
       .getBytes
       (.digest (MessageDigest/getInstance "MD5"))
       (BigInteger. 1)
       (format "%032x")))

(comment
  (md5 "abcdef609043")
  (cs/starts-with? (md5 "abcdef609043") "00000")
  (first
    (for [i (range)
          :when (cs/starts-with? (md5 (str "pqrstuv" i)) "00000")]
     i))

  (first
    (for [i (range)
          :when (cs/starts-with? (md5 (str "ckczppom" i)) "00000")]
      i))

  (first
    (for [i (range)
          :when (cs/starts-with? (md5 (str "ckczppom" i)) "000000")]
      i)))
