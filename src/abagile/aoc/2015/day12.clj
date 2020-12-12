(ns abagile.aoc.2015.day12
  (:gen-class)
  (:require
    [clojure.data.json :as json]
    [clojure.java.io :as io]))

(def sample1 "[1,2,3]")
(def sample2 "{\"a\": 2 \"b\":4}")
(def sample3 "[1,{\"c\":\"red\",\"b\":2},3]")
(def sample4 "{\"d\":\"red\",\"e\":[1,2,3,4],\"f\":5}")
(def sample5 "[1,\"red\",5]")

(re-seq #"[+-]?\d+" sample1)
(re-seq #"[+-]?\d+" sample2)

(defn deep-find-no [x]
  (cond
    (map? x)
    (when-not ((set (vals x)) "red")
      (let [nos (filter number? (vals x))
            colls (filter coll? (vals x))]
        (concat nos (when (seq colls) (mapcat deep-find-no colls)))))

    (coll? x)
    (let [nos (filter number? x)
          colls (filter coll? x)]
      (concat nos (when (seq colls) (mapcat deep-find-no colls))))))

(comment
  (deep-find-no [12 34 45 [23 [34 [14] 38] 349 "abc"]])
  (deep-find-no {:a 1 :b {:c 2 :x "red"}})
  (deep-find-no [{:a {:b 120} :c [34 45]}])
  (deep-find-no (json/read-str sample3))
  (deep-find-no (json/read-str sample4))
  (deep-find-no (json/read-str sample5)))

(defn -main [& _]
  (println "part 1:"
           (->> (slurp (io/resource "2015/day12.txt"))
                (re-seq #"[+-]?\d+")
                (map read-string)
                (reduce +)))

  (println "part 2:"
           (->> (slurp (io/resource "2015/day12.txt"))
                (json/read-str)
                deep-find-no
                (reduce +))))
