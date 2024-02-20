package com.example.command;

import com.aliyun.devops20210625.Client;
import com.aliyun.devops20210625.models.CreateMergeRequestRequest;
import com.aliyun.devops20210625.models.CreateMergeRequestResponse;
import com.example.util.BranchUtil;
import com.example.util.ClientUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MergeRequestCommand implements Command {
    @Override
    public void execute(String[] args) {
        try {
            Client client = ClientUtil.getClient();
            if (args.length != 5) {
                throw new RuntimeException("Invalid args");
            }
            Long repositoryId = Long.valueOf(args[1]);
            String sourceBranch = args[2];
            String targetBranch = args[3];
            String title = args[4];

            CreateMergeRequestRequest request = BranchUtil.mmm(repositoryId, sourceBranch, targetBranch, title);
            CreateMergeRequestResponse response;
            try {
                response = client.createMergeRequest(String.valueOf(repositoryId), request);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            Long localId = response.getBody().getResult().getLocalId();
            log.info("Create merge request success, localId: {}", localId);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String helpMessage() {
        return "repositoryId sourceBranch targetBranch title\n\t\tCreate merge request.";
    }

    @Override
    public String option() {
        return "m";
    }
}
