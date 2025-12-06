set dotenv-load
set quiet

current_year := `date +%Y`
current_day := `date +%d`

src_dir := "cmd"
inputs_dir := "resources"

alias r := run
alias t := testrun
alias d := download
alias p := prepare

prepare day=current_day year=current_year:
    basename=`printf 'day%02d' {{day}}`; \
      mkdir -p ./{{src_dir}}/{{year}}/${basename}; \
      cp ./{{src_dir}}/main.go ./{{src_dir}}/{{year}}/${basename}/main.go

run day=current_day year=current_year:
    basename=`printf 'day%02d' {{day}}`; \
      go run ./{{src_dir}}/{{year}}/${basename} < {{inputs_dir}}/{{year}}/${basename}.txt

testrun day=current_day year=current_year:
    basename=`printf 'day%02d' {{day}}`; \
      go run ./{{src_dir}}/{{year}}/${basename} < {{inputs_dir}}/{{year}}/${basename}.sample

download day=current_day year=current_year:
    mkdir -p resources/{{year}}
    basename=`printf 'day%02d' {{day}}`; \
      day_str=`echo {{day}} | sed 's/^0//'`; \
        curl -sS -b "session=$AOC_SESSION" \
          "https://adventofcode.com/{{year}}/day/${day_str}/input" \
          -o {{inputs_dir}}/{{year}}/${basename}.txt
