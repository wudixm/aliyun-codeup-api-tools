# aliyun-codeup-api-tools


```shell
usage: java -jar aliyun-codeup-api-tools-0.0.1-SNAPSHOT.jar [-a] [-b] [-h] [-m] [-r]
Options:
 -a   [orderByColumn]
      Get all repository, The supported columns are:
      id	repository id.
      name
      repository name. (default)
 -b   repositoryId [page] [pageSize]
      Get branches of the repository
 -h   print this message
 -m   repositoryId sourceBranch targetBranch title
      Create merge request.
 -r   [page] [pageSize]
      List repository
```