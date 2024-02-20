package com.example.command;

import com.aliyun.devops20210625.Client;
import com.aliyun.devops20210625.models.ListRepositoryBranchesResponseBody;
import com.example.util.BranchUtil;
import com.example.util.ClientUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class BranchCommand implements Command {
    @Override
    public void execute(String[] args) {
        // "-b repositoryId [page] [pageSize]"
        try {
            Client client = ClientUtil.getClient();
            Long repositoryId = Long.parseLong(args[1]);
            Long page;
            Long pageSize = 10L;
            if (args.length == 3) {
                page = Long.parseLong(args[2]);
            } else if (args.length == 4) {
                page = Long.parseLong(args[2]);
                pageSize = Long.parseLong(args[3]);
            } else {
                log.error("args length error");
                return;
            }

            List<ListRepositoryBranchesResponseBody.ListRepositoryBranchesResponseBodyResult> branches = BranchUtil.getBranches(client, repositoryId, page, pageSize);

            StringBuilder sb = new StringBuilder();
            sb.append("branches: \n");
            for (ListRepositoryBranchesResponseBody.ListRepositoryBranchesResponseBodyResult branch : branches) {
                sb.append(branch.getName()).append("\n");
            }
            log.info(sb.toString());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String helpMessage() {
        return "repositoryId [page] [pageSize]\n\t\tGet branches of the repository";
    }

    @Override
    public String option() {
        return "b";
    }
}
