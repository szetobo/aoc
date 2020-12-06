(ns abagile.aoc.2015.day7
  (:gen-class)
  (:require
    [clojure.java.io :as io]
    [clojure.string :as cs]))

(defn input [] (cs/split-lines (slurp (io/resource "2015/day7.txt"))))

(def to-param #(if-not (re-matches #"^\d+$" %) % (Integer/parseInt %)))

(comment
  (count (input)))

(defn parse [wire]
  (condp re-matches wire
    #"^(\w+)\s*->\s*(\w+)$"       :>> (fn [[_ p g]] {:gate g :fn identity :params [(to-param p)]})
    #"^NOT\s+(\w+)\s*->\s*(\w+)$" :>> (fn [[_ p g]] {:gate g :fn bit-not :params [(to-param p)]})
    #"^(\w+)\s+AND\s+(\w+)\s*->\s*(\w+)$" :>> (fn [[_ p1 p2 g]] {:gate g :fn bit-and :params [(to-param p1) (to-param p2)]})
    #"^(\w+)\s+OR\s+(\w+)\s*->\s*(\w+)$"  :>> (fn [[_ p1 p2 g]] {:gate g :fn bit-or  :params [(to-param p1) (to-param p2)]})
    #"^(\w+)\s+LSHIFT\s+(\d+)\s*->\s*(\w+)$"  :>> (fn [[_ p c g]] {:gate g :fn bit-shift-left  :params [(to-param p) (to-param c)]})
    #"^(\w+)\s+RSHIFT\s+(\d+)\s*->\s*(\w+)$"  :>> (fn [[_ p c g]] {:gate g :fn bit-shift-right :params [(to-param p )(to-param c)]})))

(comment
  (parse "123 -> x")
  (parse "x AND y -> d")
  (parse "x OR y -> d")
  (parse "x LSHIFT 2 -> f")
  (parse "y RSHIFT 2 -> g")
  (parse "NOT x -> h"))

(defn eval-params [env param] (get env param param))
(defn params-ready [env params] (every? #(number? (eval-params env %)) params))
(defn eval-gate [env gate]
  (let [{:keys [gate fn params]} gate]
    (assoc-in env [gate] (apply fn (map #(eval-params env %) params)))))

(defn eval-gates [circuit target]
  (loop [env   {}
         gates circuit]
    (if (env target)
      env
      (recur (reduce eval-gate env (filter #(params-ready env (:params %)) gates))
             (filter #(nil? (env (:gate %))) gates)))))

(comment
  (reduce eval-gate {}
          (filter #(params-ready {} (:params %)) [{:gate "b", :fn identity, :params[44430]}
                                                  {:gate "c", :fn identity, :params[123]}
                                                  {:gate "b", :fn identity, :params[123]}])))

(comment
  (-> (map parse (input))
      (eval-gates "a")
      (get "a")))
