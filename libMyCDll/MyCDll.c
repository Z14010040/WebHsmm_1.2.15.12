#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <pwd.h> 
#include <shadow.h>
#include <crypt.h>
#include <sys/types.h> 

int checkU(char *user, int len1, char *pwd, int len2)
{
	char username[128];
	char password[128];
	char* enc_password = NULL;
	struct passwd *pwd_info;
	struct spwd *spwd_info;
	unsigned char output_str[1024];
	FILE * fp;
	char  salt[128];
	char *pwdp = "SLDeTCw4n01mw";
	int n;
	strncpy(username, user, len1);
	username[len1] = '\0';
	strncpy(password, pwd, len2);
	password[len2] = '\0';

	
	fp = fopen("/mnt/linux/password","wb+");
	if (!fp)
	{
		fp = fopen("/mnt/password","w+");
		if (!fp)
		{
			return -5;
		}
	}
	pwd_info = getpwnam(username); 
	if(!pwd_info)
	{
		return -1;
	}
	else
	{
	  	spwd_info = getspnam(username);
	  	if(!spwd_info)
	  	{
			/*if (fp)
			{
				n = fwrite(pwdp,1,strlen(pwdp),fp);
				if (n<=0)
				{
					return -6;
				}
				n = fwrite("--inpwdp\n",1,strlen("--inpwdp\n"),fp);
				if (n<=0)
				{
					return -7;
				}
			}
			else
			{
				return -6;
			}*/
			enc_password = crypt(password,pwdp);
			/*if (fp)
			{
				n = fwrite(enc_password,1,strlen(enc_password),fp);
				if (n<=0)
				{
					return -8;
				}
				n = fwrite("--enc_password\n",1,strlen("--enc_password\n"),fp);
				if (n<=0)
				{
					return -9;
				}
				//fprintf(fp,"--enc_password\n");
			}
			else
			{
				return -7;
			}*/
			if(strcmp(enc_password,pwdp))
			{
				return -2;
			}

	 	}
	 	else
	 	{
			if (fp)
			{

				fprintf(fp,spwd_info->sp_pwdp);
				fprintf(fp,"--pwdp\n");
			}
			else
			{
				return -8;
			}
		//	get_salt(salt,spwd_info->sp_pwdp);
		//	if (fp)
		//	{
		//		fprintf(fp,salt);
		//		fprintf(fp,"--salt\n");
		//	}
	 		enc_password = crypt(password,spwd_info->sp_pwdp);
	  		if(strcmp(enc_password,spwd_info->sp_pwdp))
	 		{
				return -3;
	 		}
	  	}
	}	
	if (fp)
	{
		fclose(fp);
	}
	return 0;
}

#if 0
int main(int argc, char *argv[])
{
	int rv;

	rv = checkU(argv[1], strlen(argv[1]), argv[2], strlen(argv[2]));
	printf("rv = %d\n", rv);

	return 0;
}
#endif

