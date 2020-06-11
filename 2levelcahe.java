
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class bonus {
    static class Node {
        String key;
        String data;
        int dirty;
        Node pre;
        Node next;

        public Node(int dirty, String key, String data) {
            this.dirty = dirty;
            this.key = key;
            this.data = data;
        }
    }

    static class LRU {
//   LRU stands for least recently used
//   implemented using double linked list and hashmap
        HashMap<String, Node> map;
        int capacity, count;
        Node l2;
        Node head, tail;

        LRU(int capacity) {
            //            initialising the value

            this.capacity = capacity;
            map = new HashMap<>();
            head = new Node(0, "-", "-");
            tail = new Node(0, "-", "-");
            l2 = new Node(0, "-", "-");
            head.next = tail;
            tail.pre = head;
            head.pre = null;
            tail.next = null;
            count = 0;
        }

        public void deleteNode(Node node) {
//        delete the a node from the double linked list

            node.pre.next = node.next;
            node.next.pre = node.pre;
        }

        public void print() {
//            print the hashmap which stores the value of the cache

            for (String name : map.keySet()) {
                String key = name.toString();
                Node value = map.get(name);
                System.out.println(value.key + " " + value.data);
            }
        }

        public void addtohead(Node node) {
            //           add the node to the from of the double linked list

            node.next = head.next;
            node.next.pre = node;
            node.pre = head;
            head.next = node;
        }

        public String get(String key) {
            //            retrive data from the cache using a given address

            if (map.get(key) == null) {
                System.out.println("Did not get any value" +
                        " for the key: " + key);
                return "null";
            }
//                searching if the key is present in the hashmap
//                if not just return null
//                else return the data
            Node node = map.get(key);
            String result = node.data;
            deleteNode(node);
            addtohead(node);
            System.out.println("Got value : " + result + " for key: " + key);
            return result;
        }

        public void set(String key, String data) {
            //            assign the key with data in the cache

            System.out.println("Going to set the (key, " +
                    "value) : (" + key + ", " + data + ")");
            if (map.get(key) == null) {
                //                if the key/address does not exist in the cache we add it
//                in the cache
                Node node = new Node(0, key, data);
                if (data.equals("-")) {
                    node.dirty = 0;
                } else {
                    node.dirty = 1;
                }
                map.put(key, node);
                // if the cache is full we remove the last element
//                from it because it is the least recently used
                if (count < capacity) {
                    count++;
                } else {
                    // l2 is used to store the node which was removed from the cache so that
                    // it could be added to l2
                    System.out.println("the lru address was removed" + tail.pre.key);
                    map.remove(tail.pre.key);
                    deleteNode(tail.pre);
                    l2 = tail.pre;
                }
                addtohead(node);
                // if any other value exist in the given key we replace the
//                value with the new value

            } else {
                Node node = map.get(key);
                l2 = node;
                System.out.println("Data is replaced with" + node.data);
                node.dirty = 1;
                node.data = data;
                //                when data is added we take the node and move it in the front

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
        // taking input of size of memory in bits,
//        cache size in kb
//        and block size in bytes
        int mm_Size, cache_size, block_size;
        System.out.println("Main memory in bit: ");
        mm_Size = scanner.nextInt();
        System.out.println("cache_size in KB: ");
        cache_size = scanner.nextInt();
        System.out.println("block_size in bytes: ");
        block_size = scanner.nextInt();
        int temp = log2(cache_size) + 10 - log2(block_size);
        int no_of_block = (int) Math.pow(2, temp);
        //        to calculate the number of lines in the cache
//        we divide the cache size by the block size
//        I have calculated this by converting everything in the power of 2(binary)
        LRU cache = new LRU(no_of_block);
        LRU l2 = new LRU(2 * no_of_block);
        // the hashmap is used to store which address has been used
        HashMap<String, Integer> tag_count = new HashMap<String, Integer>();
        while (true) {
            String address;
            System.out.println("enter an address of size " + (mm_Size) + " : ");
            address = scanner.next();
            // dividing the address into 2 parts
            int offset_bit = log2(block_size);
            int tag_bit = mm_Size - log2(block_size);
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
            System.out.println("No of offset bit will be " + (log2(block_size)));
            System.out.println("Tag bit =" + (mm_Size - log2(block_size)));
            String offset = address.substring(tag_bit);
            String tag = address.substring(0, tag_bit);
            String in;
            System.out.println("String of length " + block_size + " : ");
            in = scanner.next();
            if(in.length() != block_size){
                System.out.println("Please check the size of the data");
                continue;
            }
            String ans;
            System.out.println("Do you want to read/write data?");
            ans = scanner.next();

            if (ans.equals("write")) {

                // if the key already exist in the hashmap then do not add in the cache
//                if(tag_count.containsKey(tag)){
//
//                }
                if (tag_count.get(tag) == null) {
                    System.out.println("l2");
                    l2.set(tag, in);
                }
                tag_count.put(tag, 1);
                System.out.println("l1");
                cache.set(tag, in);
                // if their is ssan overflow in l1 then the removed data is stored in the l2 cache
                if (cache.count >= cache.capacity) {
                    System.out.println("l2");
                    l2.set(cache.l2.key, cache.l2.data);
                }
            } else if(ans.equals("read")) {
                // when we cannot find an address in the primary cache then we search for it in the secondary cache and retrive the data from their

                if (cache.get(tag).equals("null")) {
                    System.out.println("miss");
                    System.out.println("Searching for the address in l2 cache ");
                    System.out.println(l2.get(tag).charAt(binarytode(offset)));


                } else {
                    System.out.println("hit");
                    System.out.println(cache.get(tag).charAt(binarytode(offset)));

                }
            }else{
                System.out.println("please input a valid input");
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
                System.out.println("L1");
                cache.print();
                System.out.println("l2");
                l2.print();
            }
        }
    }

    public static void kwayassociative() {
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
        ArrayList<LRU> cache = new ArrayList<LRU>(s);
        for (int i = 0; i < s; i++) {
            cache.add(new LRU(k));
        }
        ArrayList<LRU> l2 = new ArrayList<LRU>(s);
        for (int i = 0; i < s; i++) {
            l2.add(new LRU(2 * k));
        }
        HashMap<String, String> tag_count = new HashMap<>();
        while (true) {
            String address;
            System.out.println("enter an address of size " + (mm_Size) + " : ");
            address = scanner.next();
            int offset_bit = log2(block_size);

            int index_bit = log2(no_of_block / k);
            int k_bit = log2(k);
            int tag_bit = mm_Size - offset_bit - k_bit - index_bit;
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
                // if the address is already in hashmap we do not add it in l2
                if(tag_count.containsKey(tag + kway)){
                    if (!tag_count.get(tag + kway).equals(index)) {
                        System.out.println("l2");
//                        System.out.println(binarytode(index));
                        l2.get(binarytode(index)).set(tag + kway, in);
                    }
                }else{
                    System.out.println("l2");
//                        System.out.println(binarytode(index));
                    l2.get(binarytode(index)).set(tag + kway, in);
                }

                tag_count.put(tag + kway, index);
                System.out.println("l1");
                cache.get(binarytode(index)).set(tag + kway, in);
// if their is an overflow than we store the removed address in l2
                if (cache.get(binarytode(index)).count >= cache.get(binarytode(index)).capacity) {
                    System.out.println("l2");
                    l2.get(binarytode(index)).set(cache.get(binarytode(index)).l2.key, cache.get(binarytode(index)).l2.data);
                }
            } else if(ans.equals("read")) {
                // if their is a miss we look for the address in l2
                if (cache.get(binarytode(index)).get(tag + kway).equals("null")) {
                    System.out.println("miss");
                    System.out.println("Searching for the address in l2 cache ");
                    System.out.println(l2.get(binarytode(index)).get(tag + kway).charAt(binarytode(offset)));
                } else {
                    System.out.println("hit");
                    System.out.println(cache.get(binarytode(index)).get(tag + kway).charAt(binarytode(offset)));
                }
            }else{
                System.out.println("please input a valid input");
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
                System.out.println("L1");
                for (int i = 0; i < cache.size(); i++) {
                    cache.get(i).print();
                }
                System.out.println("L2");
                for (int i = 0; i < cache.size(); i++) {
                    l2.get(i).print();
                }
            }
        }
    }

    public static void directmapping() {
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
        ArrayList<String> tag_array = new ArrayList<String>(no_of_block);
        ArrayList<String> data_array = new ArrayList<String>(no_of_block);
        LRU l2 = new LRU(2 * no_of_block);
        HashMap<String, String> tag_count = new HashMap<>();
        for (int i = 0; i < no_of_block; i++) {
            tag_array.add("-");
            data_array.add("-");
        }
        while (true) {
            String address;
            System.out.println("enter an address of size " + (mm_Size) + " : ");
            address = scanner.next();
            int offset_bit = log2(block_size);
            int line_bit = log2(no_of_block);
            int tag_bit = mm_Size - offset_bit - line_bit;
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
            String tag = address.substring(0, tag_bit);
            String line = address.substring(tag_bit, tag_bit + line_bit);
            String offset = address.substring(tag_bit + line_bit);
            System.out.println("tag bit " + tag_bit);
            System.out.println("line_bit " + line_bit);
            System.out.println("offset_bit " + offset_bit);
            // implmented the second cache as associative
            String ans;
            System.out.println("Do you want to read/write data?");
            ans = scanner.next();
            if (ans.equals("write")) {
                System.out.println("String of length " + (block_size) + " : ");
                String in = scanner.next();
                if(in.length() != block_size){
                    System.out.println("Please check the size of the data");
                    continue;
                }
                if (data_array.get(binarytode(line)).equals("-")) {
                    tag_array.set(binarytode(line), tag);
                    data_array.set(binarytode(line), in);
                } else {
                    System.out.println("The data is replaced on address" + address);
                    tag_array.set(binarytode(line), tag);
                    data_array.set(binarytode(line), in);
                }
                if(tag_count.containsKey(tag)){
                    if (!tag_count.get(tag).equals(line)) {
                        System.out.println("l2");
                        l2.set(tag + line, in);
                    }
                }else{
                    System.out.println("l2");
                    l2.set(tag + line, in);
                }

                tag_count.put(tag, line);

            } else if(ans.equals("read")){
                if (tag_array.get(binarytode(line)).equals(tag)) {
                    System.out.println("hit");
                    System.out.println(data_array.get(binarytode(line)).charAt(binarytode(offset)));
                } else {
                    System.out.println("miss");
                    System.out.println("Searching for the data in l2");
                    System.out.println(l2.get(tag + line).charAt(binarytode(offset)));
                }
            }else{
                System.out.println("please input a valid input");
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
                System.out.println("l1");
                for (int i = 0; i < no_of_block; i++) {
                    if (!data_array.get(i).equals("-")) {
                        System.out.println(tag_array.get(i) + " " + data_array.get(i));
                    }
                }
                System.out.println("l2");
                l2.print();
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
