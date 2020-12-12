(ns abagile.aoc.2015.day10
  (:gen-class))
  ; (:require
  ;   [clojure.java.io :as io]
  ;   [clojure.string :as cs]))

(def input "1113222113")

(def sample "111221")

(defn game1 [input]
  (loop [[h & t] (seq input)
         {:keys [ch cnt res] :as ctx} {:cnt 0 :res ""}]
    (if (nil? h)
      (-> ctx
          (assoc :cnt 0 :res (str res cnt ch))
          (dissoc :ch))
      (recur t (if (nil? ch)
                 (assoc ctx :ch h :cnt 1)
                 (if (= h ch)
                   (update ctx :cnt inc)
                   (-> ctx
                       (assoc :res (str res cnt ch))
                       (assoc :ch h :cnt 1))))))))

(defn game-x [coll]
  (mapcat (juxt count first) (partition-by identity coll)))

;                   11132-22113
;                   31133-22113
;                 1321232-22113
;           1113121112133-22113
;       31131112311211232-22113
; 13211331121321122112133-22113

(game1 sample)

(defn -main [& _]
  (println "part 1:"
           (->> (seq input)
                (map #(- (int %) 48))
                (iterate game-x)
                (#(nth % 40))
                count))
           ; (count (reduce (fn [res _] (-> (game1 res) :res)) (seq input) (range 40))))

  (println "part 2:"
           (->> (seq input)
               (map #(- (int %) 48))
               (iterate game-x)
               (#(nth % 50))
               count)))
