#include <stdio.h>

struct dynar_int {
	int* array;
	int len;
	int size;
};
void manage_memory_int(struct dynar_int *dynar_NAME) {
    if(dynar_NAME->len == dynar_NAME->size) {
        dynar_NAME->size += 15;
        int* tempArray = (int *)malloc(dynar_NAME->size * sizeof(int));
        for(int i = 0; i < dynar_NAME->len; i++) {
            tempArray[i] = dynar_NAME->array[i];
        }
        free(dynar_NAME->array);
        dynar_NAME->array = tempArray;
        tempArray = NULL;
        free(tempArray);
    }

    else if(dynar_NAME->len == dynar_NAME->size / 2) {
        dynar_NAME->size = (dynar_NAME->size / 2) + 5;
        int* tempArray = (int *)malloc(dynar_NAME->size * sizeof(int));
        for(int i = 0; i < dynar_NAME->len; i++) {
            tempArray[i] = dynar_NAME->array[i];
        }
        free(dynar_NAME->array);
        dynar_NAME->array = tempArray;
        tempArray = NULL;
        free(tempArray);
    }
}

void pop_int(struct dynar_int *dynar_NAME) {
    manage_memory_int(dynar_NAME);
    dynar_NAME->array[dynar_NAME->len - 1] = 0;
    dynar_NAME->len--;
}

void del_int(struct dynar_int *dynar_NAME, int index) {
    if(index >= dynar_NAME->len)
        printf("Tyring to delete a non-existent element !");
    else {
        if(index == dynar_NAME->len - 1) {
            manage_memory_int(dynar_NAME);
            dynar_NAME->len--;
        }
        else {
            manage_memory_int(dynar_NAME);
            for(int l = index; l < dynar_NAME->len - 1; l++) {
                dynar_NAME->array[l] = dynar_NAME->array[l + 1];
            }
            dynar_NAME->len--;
        }
    }
}

void push_int(struct dynar_int *dynar_NAME, int value) {
    manage_memory_int(dynar_NAME);
    dynar_NAME->array[dynar_NAME->len] = value;
    dynar_NAME->len++;
}

void insert_int(struct dynar_int *dynar_NAME, int value, int index) {
    if(index > dynar_NAME->len)
        printf("Trying to insert an element outside the array !");
    else {
        manage_memory_int(dynar_NAME);
            for(int l = dynar_NAME->len; l > index; l--) {
                dynar_NAME->array[l] = dynar_NAME->array[l - 1];
            }
        dynar_NAME->array[index] = value;
        dynar_NAME->len++;
    }
}

void delN_int(struct dynar_int *dynar_NAME, int value, int number) {
    if(number >= dynar_NAME->len)
        printf("Trying to delete more elements than there are in the array !");
    else {
        int i = 0;
        int j = 0;
        for(; i < dynar_NAME->len && j < number; ) {
            if(dynar_NAME->array[i] == value) {
                for(int l = i; l < dynar_NAME->len - 1; l++) {
                    dynar_NAME->array[l] = dynar_NAME->array[l + 1];
                }
                dynar_NAME->len--;
                j++;
                i = 0;
            }
            else i++;
        }
    }
}

int main() {
struct dynar_int numbers;
numbers.array = (int *)malloc(10 * sizeof(int));
numbers.len = 0;
numbers.size = 10;
numbers.array[0] = 1;
numbers.array[1] =  1;
numbers.array[2] =  2;
numbers.array[3] =  3;
numbers.array[4] =  4;
numbers.array[5] =  5;
numbers.len = 6;

for(int i = 0; i < numbers.len; i++) printf("%d", numbers.array[i]); printf("\n");

pop_int(&numbers);

printf("%d", numbers.len);
printf("\n");

for(int i = 0; i < numbers.len; i++) printf("%d", numbers.array[i]); printf("\n");

push_int(&numbers, 6);

for(int i = 0; i < numbers.len; i++) printf("%d", numbers.array[i]); printf("\n");

delN_int(&numbers, 1, 2);

for(int i = 0; i < numbers.len; i++) printf("%d", numbers.array[i]); printf("\n");
printf("%d", numbers.len);
printf("\n");

}

