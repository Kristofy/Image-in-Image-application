#include <stdexcept>
#include <stdio.h>
#include <string>
#include <string.h>

using namespace std;

bool ok = 1;
const char chk[] = "Image rendered!";

void track(string command) {
	char buffer[128];

	// Open pipe to file
	FILE* pipe = popen(command.c_str(), "r");
	if (!pipe) {
		puts("Error, can not track Process!");
        return;
	}
	puts("0% done");
	// read till end of process:
	while (!feof(pipe)) {

		// use buffer to read and add to result
		if (fgets(buffer, 128, pipe) != NULL){
			if(strncmp(buffer, chk, strlen(chk)) == 0) ok = 0;
			printf("%s",buffer);		
		}
		
	}

	pclose(pipe);
}

int main()
{
  puts("Rendering started");
  track("ImageInImage.exe");
  return (int)ok;
}
