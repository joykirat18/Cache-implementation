

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    static class Node {
        String key;
        String data;
        Node pre;
        Node next;

        public Node(String key, String data) {
            this.key = key;
            this.data = data;
        }
    }

    static class LRU {
        //   LRU stands for least recently used
//        implemented using double linked list and hashmap
        HashMap<String, Node> map;
        int capacity, count;
        Node head, tail;

        LRU(int capacity) {
//            initialising the value
            map = new HashMap<>();
            head = new Node("-", "-");
            tail = new Node("-", "-");
            head.next = tail;
            tail.pre = head;
            head.pre = null;
            tail.next = null;

            this.capacity = capacity;
            count = 0;
        }

        public void deleteNode(Node node) {
//        delete the a node from the double linked list
            node.pre.next = node.next;
            node.next.pre = node.pre;
        }

        public void addtohead(Node node) {
//           add the node to the from of the double linked list
            node.next = head.next;
            node.next.pre = node;
            node.pre = head;
            head.next = node;
        }

        public void print() {
//            print the hashmap which stores the value of the cache
            for (String name : map.keySet()) {
                String key = name.toString();
                Node value = map.get(name);
                System.out.println(value.key + " " + value.data);
            }
        }

        public String get(String key) {
//            retrive data from the cache using a given address
            if (map.get(key) == null) {
//                searching if the key is present in the hashmap
//                if not just return null
//                else return the data
                System.out.println("Did not get any value" +
                        " for the key: " + key);
                return "null";
            }
//            since this node was called therefore
//            the node is displaced form its current position
//            and moved to the front
            Node node = map.get(key);
            String result = node.data;
            deleteNode(node);
            addtohead(node);
            System.out.println("Got the value : " +
                    result + " for the key: " + key);
            return result;
        }

        public void set(String key, String data) {
//            assign the key with data in the cache
            System.out.println("Going to set the (key, " +
                    "value) : (" + key + ", " + data + ")");

            if (map.get(key) == null) {
//                if the key/address does not exist in the cache we add it
//                in the cache
                Node node = new Node(key, data);
                map.put(key, node);
//                if the cache is full we remove the last element
//                from it because it is the least recently used
                if (count < capacity) {
                    count++;
                } else {
                    System.out.println("the lru address was removed" + tail.pre.key);
                    map.remove(tail.pre.key);
                    deleteNode(tail.pre);
                }
                addtohead(node);
//                if any other value exist in the given key we replace the
//                value with the new value
            } else {
                Node node = map.get(key);
                System.out.println("Data is replaced with" + node.data);
//                when data is added we take the node and move it in the front
                node.data = data;
                deleteNode(node);
                addtohead(node);
            }
        }
    }

    public static int log2(int N) {
//        helps the find log2 of a number
        return (int) (Math.log(N) / Math.log(2));
    }

    public static int binarytode(String s) {
//        converts binary to its decimal value
        int num = Integer.parseInt(s);
        int dec_value = 0;
        int base = 1;
        int temp = num;
        while (temp > 0) {
            int last_digit = temp % 10;
            temp = temp / 10;
            dec_value += last_digit * base;
            base = base * 2;
        }
        return dec_value;
    }

    public static void fullAssociative() {
//        here is the implementation of a full associative
        Scanner scanner = new Scanner(System.in);
//        taking input of size of memory in bits,
//        cache size in kb
//        and block size in bytes
        int mm_Size, cache_size, block_size;
        System.out.println("Main memory in bit: ");
        mm_Size = scanner.nextInt();
        System.out.println("cache_size in KB: ");
        cache_size = scanner.nextInt();
        System.out.println("block_size in bytes: ");
        block_size = scanner.nextInt();

//        to calculate the number of lines in the cache
//        we divide the cache size by the block size
//        I have calculated this by converting everything in the power of 2(binary)

        int temp = log2(cache_size) + 10 - log2(block_size);
        int no_of_block = (int) Math.pow(2, temp);

//        creating a cache of size equal to the number of blocks

        LRU cache = new LRU(no_of_block);
        while (true) {
            String address;
            System.out.println("enter an address of size " + (mm_Size) + " : ");
            address = scanner.next();
            if (address.length() != mm_Size) {
                System.out.println("Please check the address size");
                continue;
            }
            char[] ch = address.toCharArray();
            for (int i = 0; i < ch.length; i++) {
                if (ch[i] != '0' && ch[i] != '1') {
                    System.out.println("please enter address only in 0's and 1's");
                    return;
                }
            }
//            dividing the address into its offset bit and tag bit
            int offset_bit = log2(block_size);

            int tag_bit = mm_Size - log2(block_size);
            if (tag_bit < 0) {
                System.out.println("PLease print a valid address as it cannot accommodate the offset bits");
                break;
            }
            System.out.println("No of offset bit will be " + (log2(block_size)));
            System.out.println("Tag bit =" + (mm_Size - log2(block_size)));
            String offset = address.substring(tag_bit);
            String tag = address.substring(0, tag_bit);
//            now i take the option of read or write the data from the cache
            String ans;
            System.out.println("Do you want to read/write data?");
            ans = scanner.next();


            if (ans.equals("write")) {
                String in;
                System.out.println("String of length " + (block_size) + " : ");
                in = scanner.next();
                if(in.length() != block_size){
                    System.out.println("Please check the size of the data");
                    continue;
                }
//                if write i simply add the data with its corresponding key
                cache.set(tag, in);
            } else if (ans.equals("read")) {
//                if we choose read and the address does not exist in the cache
//                then this is a miss
                if (cache.get(tag).equals("null")) {
                    System.out.println("miss");
                } else {
                    System.out.println("hit");
                    System.out.println(cache.get(tag).charAt(binarytode(offset)));
                }
            }else{
                System.out.println("please input a valid input");
                continue;
            }
            String a;
//            option to exit from the program
            System.out.println("Do you want to exit(Y/N)?");
            a = scanner.next();
            if (a.equals("Y")) {
                return;
            }
//            option to print the cache(sometimes cache can be of huge size thats why it is an option)
            System.out.println("Do you want to print the cache (Y/N)");
            String p = scanner.next();
            if (p.equals("Y")) {
                cache.print();
            }
        }
    }

    public static void kwayassociative() {
//        taking input same as fully associative
        Scanner scanner = new Scanner(System.in);
        int mm_Size, cache_size, block_size, k;
        System.out.println("Main memory in bit: ");
        mm_Size = scanner.nextInt();
        System.out.println("cache_size in KB: ");
        cache_size = scanner.nextInt();
        System.out.println("block_size in bytes: ");
        block_size = scanner.nextInt();
        System.out.println("Value of k ");
        k = scanner.nextInt();
        int temp = (log2(cache_size) + 10 - log2(block_size));
        int no_of_block = (int) Math.pow(2, temp);
        int s = no_of_block / k;
//        created an array which each contains a cache of size k
//        each individual cache ast as a fully associative caache of size k
//        basically it is a combination of fully associative and direct mapping
        ArrayList<LRU> cache = new ArrayList<LRU>(s);
        for (int i = 0; i < s; i++) {
            cache.add(new LRU(k));
        }
        while (true) {
            String address;
            System.out.println("enter an address of size " + (mm_Size) + " : ");
            address = scanner.next();
            if (address.length() != mm_Size) {
                System.out.println("Please check the address size");
                continue;
            }
            char[] ch = address.toCharArray();
            for (int i = 0; i < ch.length; i++) {
                if (ch[i] != '0' && ch[i] != '1') {
                    System.out.println("please enter address only in 0's and 1's");
                    break;
                }
            }
//            divide the address into 4 different parts
            int offset_bit = log2(block_size);
            int index_bit = log2(no_of_block / k);
            int k_bit = log2(k);
            int tag_bit = mm_Size - offset_bit - k_bit - index_bit;
            if (tag_bit <= 0) {
                System.out.println("please rerun the program as the size of address cannot accommodate all the bits");
                break;
            }
            String offset = address.substring(tag_bit + k_bit + index_bit);
            String index = address.substring(tag_bit + k_bit, mm_Size - offset_bit);
            String kway = address.substring(tag_bit, tag_bit + k_bit);
            String tag = address.substring(0, tag_bit);
            System.out.println("tag bit " + tag_bit);
            System.out.println("k way bit " + k_bit);
            System.out.println("index bit " + index_bit);
            System.out.println("offset_bit " + offset_bit);
            String ans;
            System.out.println("Do you want to read/write data?");
            ans = scanner.next();
            if (ans.equals("write")) {
                String in;
                System.out.println("String of length " + (block_size) + " : ");
                in = scanner.next();
                if(in.length() != block_size){
                    System.out.println("Please check the size of the data");
                    continue;
                }
//                the index tells me which cache out of the (cache_line/k) to choose
//                and then add the key according to LRU
                System.out.println(index);
                System.out.println(binarytode(index));
                cache.get(binarytode(index)).set(tag + kway, in);
            } else if(ans.equals("read")) {
//                if we choose read and we do not find any address in the corresponding index
//                then we print miss
                if (cache.get(binarytode(index)).get(tag + kway).equals("null")) {
                    System.out.println("miss");
                } else {
//                    elses it is a hit and we print out the data we were looking for using the offset bit
                    System.out.println("hit");
                    System.out.println("value is: ");
                    System.out.println(cache.get(binarytode(index)).get(tag + kway).charAt(binarytode(offset)));
                }
            }else{
                System.out.println("please input a valid command");
                continue;
            }


            String a;
            System.out.println("Do you want to exit(Y/N)?");
            a = scanner.next();
            if (a.equals("Y")) {
                return;
            }
            System.out.println("Do you want to print the cache (Y/N)");
            String p = scanner.next();
            if (p.equals("Y")) {
                for (int i = 0; i < cache.size(); i++) {
                    System.out.println(i);
                    cache.get(i).print();
                }

            }
        }
    }

    public static void directmapping() {
//        take input same as the above two
        Scanner scanner = new Scanner(System.in);
        int mm_Size, cache_size, block_size, k;
        System.out.println("Main memory in bit: ");
        mm_Size = scanner.nextInt();
        System.out.println("cache_size in KB: ");
        cache_size = scanner.nextInt();
        System.out.println("block_size in bytes: ");
        block_size = scanner.nextInt();
        int temp = log2(cache_size) + 10 - log2(block_size);
        int no_of_block = (int) Math.pow(2, temp);
//        create two array one to store the address and other to store its data
        ArrayList<String> tag_array = new ArrayList<String>(no_of_block);
        ArrayList<String> data_array = new ArrayList<String>(no_of_block);
        for (int i = 0; i < no_of_block; i++) {
            tag_array.add("-");
            data_array.add("-");
        }
        while (true) {
            String address;
            System.out.println("enter an address of size " + (mm_Size) + " : ");
            address = scanner.next();
            if (address.length() != mm_Size) {
                System.out.println("Please check the address size");
                continue;
            }
            char[] ch = address.toCharArray();
            for (int i = 0; i < ch.length; i++) {
                if (ch[i] != '0' && ch[i] != '1') {
                    System.out.println("please enter address only in 0's and 1's");
                    break;
                }
            }
//            dividing the array into 3 parts: offset, line, tag
            int offset_bit = log2(block_size);
            int line_bit = log2(no_of_block);
            int tag_bit = mm_Size - offset_bit - line_bit;
            if(tag_bit <= 0){
                System.out.println("please rerun the program as the size of address cannot accommodate all the bits");
            }
            String tag = address.substring(0, tag_bit);
            String line = address.substring(tag_bit, tag_bit + line_bit);
            String offset = address.substring(tag_bit + line_bit);
            System.out.println("tag bit " + tag_bit);
            System.out.println("line_bit " + line_bit);
            System.out.println("offset_bit " + offset_bit);

            String ans;
            System.out.println("Do you want to read/write data?");
            ans = scanner.next();

            if (ans.equals("write")) {
                String in;
                System.out.println("String of length " + (block_size) + " : ");
                in = scanner.next();
                if(in.length() != block_size){
                    System.out.println("Please check the size of the data");
                    continue;
                }
//                if the data is added the line is found using line_bit
                if (data_array.get(binarytode(line)).equals("-")) {
                    tag_array.set(binarytode(line), tag);
                    data_array.set(binarytode(line), in);
                } else {
//                    if a data was present it will be replaced with the new data
                    System.out.println("The data is replaced on address" + address + "with data" + data_array.get(binarytode(line)));
                    tag_array.set(binarytode(line), tag);
                    data_array.set(binarytode(line), in);
                }
            } else if(ans.equals("read")){
//                we check if the tag are equal if they are equal it is a hit and we print the data
                if (tag_array.get(binarytode(line)).equals(tag)) {
                    System.out.println("hit");
                    System.out.println(data_array.get(binarytode(line)).charAt(binarytode(offset)));
                } else {
                    System.out.println("miss");
                }
            }else{
                System.out.println("please input a valid command");
                continue;
            }
            String a;
            System.out.println("Do you want to exit(Y/N)?");
            a = scanner.next();
            if (a.equals("Y")) {
                return;
            }
            System.out.println("Do you want to print the cache (Y/N)");
            String p = scanner.next();
            if (p.equals("Y")) {
                for (int i = 0; i < no_of_block; i++) {
                    if (!data_array.get(i).equals("-")) {
                        System.out.println(tag_array.get(i) + " " + data_array.get(i));
                    }
                }
            }

        }

    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("directmapping : 1\nassocaitive mapping : 2\nkway assocaitive mapping : 3\n");
        int in = scanner.nextInt();
        if (in == 1) {
            directmapping();
        } else if (in == 2) {
            fullAssociative();
        } else {
            kwayassociative();
        }
    }
}
