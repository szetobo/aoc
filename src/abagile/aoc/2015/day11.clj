(ns abagile.aoc.2015.day11
  (:gen-class))
  ; (:require
  ;   [clojure.java.io :as io]
  ;   [clojure.string :as cs]))

(def input "hxbxwxba")

(def sample1 "abcdefgh")
(def sample2 "ghijklmn")

(def succ-digit {\a \b \b \c \c \d \d \e \e \f \f \g \g \h \h \j \i \j \j \k \k \m \l \m \m \n
                 \n \p \o \p \p \q \q \r \r \s \s \t \t \u \u \v \v \w \w \x \x \y \y \z \z \a})

(defn succ [s]
  (apply str
    (loop [[h & t] (reverse (seq s))
           res ()]
      (if (nil? h)
        (if (= (first res) \a)
          (conj res \a)
          res)
        (let [ch (succ-digit h)]
          (if (not= ch \a)
            (apply conj res ch t)
            (recur t (conj res ch))))))))

(defn valid [s]
  (and (some (fn [[x y z]] (and (= (- (int y) (int x)) 1) (= (- (int z) (int y)) 1))) (partition 3 1 s))
       (->> (re-seq #"(\w)(?=\1)" s)
            (partition 2 1)
            (#(and (> (count %) 0)
                   (every? (fn [[x y]] (not= x y)) %))))
       (not (re-seq #"[iol]" s))))

(comment
  (valid "ghjaabba")
  (valid "hxbxxzaa")

  (take 1 (filter valid (iterate succ (succ sample1))))
  (take 1 (filter valid (iterate succ (succ sample2)))))

(defn -main [& _]
  (println "part 1:"
           (take 1 (filter valid (iterate succ (succ input)))))

  (println "part 2:"
           (take 2 (filter valid (iterate succ (succ input))))))
