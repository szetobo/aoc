(ns abagile.aoc.2020.template
  (:gen-class)
  (:require
    [abagile.aoc.util :as util]
    [clojure.test :refer [deftest run-tests is]]))
    ; [instaparse.core :as insta]))

(def input (->> (util/read-input-split "2020/day22.txt" #"\n\n")))

(def sample [[9 2 6 3 1]
             [5 8 4 7 10]])

(defn parse [data]
  (->> (map #(re-seq #"\n(\d+)" %) data)
       (map #(map (comp util/parse-int second) %))))

(defn play1 [data]
  (loop [[p1, p2] data]
    (let [[h1 & r1] p1
          [h2 & r2] p2]
      (if (or (nil? h1) (nil? h2))
        [p1 p2]
        (recur (if (> h1 h2)
                 [(concat r1 [h1 h2]) r2]
                 [r1 (concat r2 [h2 h1])]))))))

(defn play2 [data seen]
  (let [[p1 p2] data
        [h1 & r1] p1
        [h2 & r2] p2]
    (cond
      (empty? p1) [1 p2]
      (empty? p2) [0 p1]
      (seen [p1 p2]) [0 p1]
      :else (let [winner (if (and (>= (count r1) h1) (>= (count r2) h2))
                           (first (play2 [r1 r2] #{}))
                           (if (> h1 h2) 0 1))
                  seen (conj seen [p1 p2])]
               (if (zero? winner)
                 (recur [(concat r1 [h1 h2]) r2] seen)
                 (recur [r1 (concat r2 [h2 h1])] seen))))))

(defn game-2 [[d1 d2] seen]
  (cond
    (empty? d1) {:winner 2 :deck d2}
    (empty? d2) {:winner 1 :deck d1}
    (seen [d1 d2]) {:winner 1 :deck d1}
    :else (let [c1 (first d1) c2 (first d2)
                d1* (subvec d1 1) d2* (subvec d2 1)
                winner (if (and (<= c1 (count d1*)) (<= c2 (count d2*)))
                         (:winner
                           (game-2 [(subvec d1* 0 c1) (subvec d2* 0 c2)] #{}))
                         (if (> c1 c2) 1 2))
                seen* (conj seen [d1 d2])]
            (case winner
              1 (recur [(conj d1* c1 c2) d2*] seen*)
              2 (recur [d1* (conj d2* c2 c1)] seen*)))))

; (game-2 (map vec (parse input)) #{})


; (play2 sample #{})
; (play2 (parse input) #{})


(defn part1 []
  (time (->> (play1 (parse input))
             ((comp reverse first #(filter some? %)))
             (map-indexed #(* (inc %1) %2))
             (reduce +))))

(defn part2 []
  (time (->> (game-2 (map vec (parse input)) #{})
             :deck
             reverse
             (map-indexed #(* (inc %1) %2))
             (reduce +))))

(defn -main [& _]
  (println "part 1:" (part1))
  (println "part 2:" (part2)))

(deftest test-sample
  (is (= 306 (->> (play1 sample)
                  ((comp reverse first #(filter some? %)))
                  (map-indexed #(* (inc %1) %2))
                  (reduce +)))))

(deftest test-input
  (is (= 1 1)))

(run-tests)
