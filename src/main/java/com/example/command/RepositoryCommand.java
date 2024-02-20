package com.example.command;

import com.aliyun.devops20210625.Client;
import com.example.dto.RepositoryDTO;
import com.example.util.ClientUtil;
import com.example.util.RepositoryUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class RepositoryCommand implements Command {
    @Override
    public void execute(String[] args) {
        try {
            Long page = 1L;
            Long pageSize = 10L;
            if (args.length == 1) {
            } else if (args.length == 2) {
                page = Long.parseLong(args[1]);
            } else if (args.length == 3) {
                page = Long.parseLong(args[1]);
                pageSize = Long.parseLong(args[2]);
            } else {
                log.error("args length error");
                return;
            }
            Client client = ClientUtil.getClient();
            List<RepositoryDTO> repository = RepositoryUtil.getRepository(client, page, pageSize);

            String s = RepositoryUtil.fillRepositoryListOrderById(repository);
            log.info(s);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String helpMessage() {
        return "[page] [pageSize]\n\t\tList repository";
    }

    @Override
    public String option() {
        return "r";
    }
}
