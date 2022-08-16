(ns user
  (:require
    [clojure.java.io :as io]
    [clojure.string :as cs]
    [debux.core]
    [nextjournal.clerk :as clerk]
    [org.httpkit.client :as http]))

(def http-header {:headers {"cookie" (str "session=" (cs/trim-newline (slurp ".session")))}})

(defn download-input [year day]
  (let [path (format "resources/%d/day%02d.txt" year day)
        url  (format "https://adventofcode.com/%d/day/%d/input" year day)]
    (if (.exists (io/file path))
      (println "Input file already exists.")
      (do
        (io/make-parents path)
        (let [{:keys [status _headers body error]} @(http/get url http-header)]
          (cond
            error             (println (str "Failed, exception is " error))
            (not= status 200) (println (str "Failed with status " status "\n" body))
            :else             (spit path body)))))))

(comment
  (clerk/serve! {:browser? true
                 :watch-paths ["src"]})
  (clerk/halt!))
