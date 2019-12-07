package com.aoc;

import java.io.IOException;
import java.util.*;

public class Day7 {
    private static int startInput;
    private static List<String> inputList = new ArrayList<>();
    private static List<String> original = new ArrayList<>();
    private static Map<Integer, State> softwareList = new HashMap<>();

    private static boolean haltReached = false;

    public static void main(String[] args) throws IOException {

//        System.out.println("Please enter input to first amplifier");
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        startInput = Integer.parseInt(br.readLine());
        startInput = 0;

        FileReader reader = new FileReader();

        List<String> day7Input = reader.read("input_day_7");
        original = day7Input;

        inputList = Arrays.asList(day7Input.get(0).split(","));

        Integer[] phaseSettings = new Integer[]{0,1,2,3,4};

        List<Integer[]> permutations = new ArrayList<>();
        generatePermutations(phaseSettings, permutations);

        int maxOutput = Integer.MIN_VALUE;
        int nextAmpInput = startInput;
        for (Integer[] permutation : permutations) {
            for (int i = 0; i < permutation.length; i++) {
                List<Integer> inputs = new ArrayList<>();

                int arg1 = permutation[i];
                int arg2 = nextAmpInput;

                inputs.add(arg1);
                inputs.add(arg2);
                nextAmpInput = runProgram(inputList, inputs, -1);
                resetSoftware();
            }
            if(nextAmpInput > maxOutput) {
                maxOutput = nextAmpInput;
            }
            nextAmpInput = startInput;
        }
        System.out.println(maxOutput);


        phaseSettings = new Integer[]{5,6,7,8,9};

        permutations = new ArrayList<>();
        generatePermutations(phaseSettings, permutations);

        maxOutput = Integer.MIN_VALUE;

        for (Integer[] phases : permutations) {
            haltReached = false;
            nextAmpInput = startInput;
            int n = 0;
            softwareList.put(0, new State(0, getOriginal()));
            softwareList.put(1, new State(0, getOriginal()));
            softwareList.put(2, new State(0, getOriginal()));
            softwareList.put(3, new State(0, getOriginal()));
            softwareList.put(4, new State(0, getOriginal()));


            while(!haltReached) {
                for (int i = 0; i < phases.length; i++) {

                    List<String> software = softwareList.get(i).getSoftware();
                    List<Integer> inputs = new ArrayList<>();

                    if(n == 0) {
                        inputs.add(phases[i]);
                        inputs.add(nextAmpInput);
                    } else {
                        inputs.add(nextAmpInput);
                    }
                    nextAmpInput = runProgram(software, inputs, i);
                    if(nextAmpInput > maxOutput) {
                        maxOutput = nextAmpInput;
                    }
                }
                n++;
            }
        }
        System.out.println(maxOutput);

    }

    private static void resetSoftware() {
        inputList = Arrays.asList(original.get(0).split(","));
    }

    private static List<String> getOriginal() {
        return Arrays.asList(original.get(0).split(","));
    }

    private static int runProgram(List<String> input, List<Integer> arguments, int softwareId) {
        int n = softwareId != -1 ? softwareList.get(softwareId).getPointer() : 0;
        int idx = 0;
        while (n < input.size()) {

            String inst = input.get(n++);

            int opCode = inst.length() > 1 ? Integer.parseInt(inst.substring(inst.length() - 2)) : Integer.parseInt(inst);

            int modeParam1 = inst.length() > 2 ? Character.getNumericValue(inst.charAt(inst.length() - 3)) : 0;
            int modeParam2 = inst.length() > 3 ? Character.getNumericValue(inst.charAt(inst.length() - 4)) : 0;

            if (opCode == 1) {

                int val1 = getValue(modeParam1, n++, input);
                int val2 = getValue(modeParam2, n++, input);
                int pos = Integer.parseInt(input.get(n++));

                input.set(pos, String.valueOf(val1 + val2));

            } else if (opCode == 2) {

                int val1 = getValue(modeParam1, n++, input);
                int val2 = getValue(modeParam2, n++, input);
                int pos = Integer.parseInt(input.get(n++));

                input.set(pos, String.valueOf(val1 * val2));
            } else if (opCode == 3) {

                int pos = Integer.parseInt(input.get(n++));
                int val = arguments.get(idx++);
                input.set(pos, String.valueOf(val));

            } else if (opCode == 4) {

                if(softwareId != -1) {
                    softwareList.get(softwareId).setPointer(n);
                }

                return getValue(modeParam1, n++, input);

            } else if (opCode == 5) {

                int val1 = getValue(modeParam1, n++, input);
                int val2 = getValue(modeParam2, n++, input);

                n = val1 != 0 ? val2 : n;

            } else if (opCode == 6) {

                int val1 = getValue(modeParam1, n++, input);
                int val2 = getValue(modeParam2, n++, input);

                n = val1 == 0 ? val2 : n;

            } else if (opCode == 7) {

                int val1 = getValue(modeParam1, n++, input);
                int val2 = getValue(modeParam2, n++, input);
                int pos = Integer.parseInt(input.get(n++));

                if(val1 < val2) {
                    input.set(pos, String.valueOf(1));
                } else {
                    input.set(pos, String.valueOf(0));
                }

            } else if (opCode == 8) {

                int val1 = getValue(modeParam1, n++, input);
                int val2 = getValue(modeParam2, n++, input);
                int pos = Integer.parseInt(input.get(n++));

                if(val1 == val2) {
                    input.set(pos, String.valueOf(1));
                } else {
                    input.set(pos, String.valueOf(0));
                }
            } else if (opCode == 99) {
                haltReached = true;
                break;
            }
        }
        return 0;
    }


    public static int getValue(int mode, int n, List<String> input) {

        if(mode == 0) {
            return Integer.parseInt(input.get(Integer.parseInt(input.get(n))));
        } else if (mode == 1) {
            return Integer.parseInt(input.get(n));
        }

        throw new RuntimeException("Illegal mode!");
    }

    public static void swap(Integer[] arr, int x, int y) {
        int temp = arr[x];
        arr[x] = arr[y];
        arr[y] = temp;
    }

    public static void generatePermutations(Integer[] arr, List<Integer[]> permutations) {
        generatePermutations(arr, 0, arr.length - 1, permutations);
    }

    public static void generatePermutations(Integer[] arr, int i, int n, List<Integer[]> permutations) {
        int j;
        if (i == n) {
            Integer[] dest = new Integer[arr.length];
            System.arraycopy( arr, 0, dest, 0, arr.length); // This right here is the dumbest shit ever in Java btw
            permutations.add(dest);
        }
        else {
            for (j = i; j <= n; j++) {
                swap(arr, i, j);
                generatePermutations(arr, i + 1, n, permutations);
                swap(arr, i, j); // backtrack
            }
        }
    }

    static class State {
        private int pointer;
        private List<String> software;

        public State(int pointer, List<String> software) {
            this.pointer = pointer;
            this.software = software;
        }

        public int getPointer() {
            return pointer;
        }

        public void setPointer(int pointer) {
            this.pointer = pointer;
        }

        public List<String> getSoftware() {
            return software;
        }

        public void setSoftware(List<String> software) {
            this.software = software;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof State)) return false;
            State state = (State) o;
            return getPointer() == state.getPointer() &&
                    Objects.equals(getSoftware(), state.getSoftware());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getPointer(), getSoftware());
        }
    }



}
