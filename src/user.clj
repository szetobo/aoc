(ns user
  (:require
    [clj-http.client :as http]
    [clojure.java.io :as io]))

(def http-header {:headers {:cookie (str "session=" (slurp ".session"))}})

(defn download-input [year day]
  (let [path (format "resources/%d/day%02d.txt" year day)
        url  (format "https://adventofcode.com/%d/day/%d/input" year day)]
    (if (.exists (io/file path))
      (prn "Input file already exists.")
      (do
        (io/make-parents path)
        (spit path (:body (http/get url http-header)))))))
