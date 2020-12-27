(ns abagile.aoc.2015.day16
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [clojure.set :as set]
    [clojure.test :refer [deftest is]]
    [instaparse.core :as insta]))

(def input (->> (util/read-input-split-lines "2015/day16.txt")))

(def target {:children 3 :cats 7 :samoyeds 2 :pomeranians 3 :akitas 0
             :vizslas 0 :goldfish 5 :trees 3 :cars 2 :perfumes 1})

(def rule-parser (insta/parser "S = <'Sue '> SUE <':'> PROPS
                                SUE = #'\\d+'
                                PROPS = PROP | PROP (<','> PROP)+
                                <space> = <#'\\s*'>
                                PROP = PT QTY
                                PT = space #'\\w+' <':'> space
                                QTY = #'[0-9]+'"))

(def transform-opts {:S vector
                     :SUE util/parse-int
                     :PROPS merge
                     :PROP hash-map
                     :PT keyword
                     :QTY util/parse-int})

(defn parse [data]
  (->> (map rule-parser data)
       (map (partial insta/transform transform-opts))))

(defn part1 []
  (time (let [ts (set target)]
         (->> (map (fn [[k v]] (when (set/subset? (set v) ts) k)) (parse input))
              (filter some?)
              first))))

(defn part2 []
  (time (->> (map
               (fn [[k v]]
                 (when (reduce (fn [res [k' v']]
                                 (let [tv (target k')]
                                   (and res (case k'
                                              :cats :trees (> v' tv)
                                              :pomeranians :goldfish (< v' tv)
                                              (= v' tv)))))
                               true v)
                   k))
               (parse input))
            (filter some?)
            first)))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(deftest test-input
  (is (= (first (parse input)) {1 {:cars 9, :goldfish 0, :akitas 3}})))
