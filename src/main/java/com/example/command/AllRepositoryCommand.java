package com.example.command;

import com.aliyun.devops20210625.Client;
import com.example.dto.RepositoryDTO;
import com.example.util.ClientUtil;
import com.example.util.RepositoryUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AllRepositoryCommand implements Command {
    @Override
    public void execute(String[] args) {
        try {
            Client client = ClientUtil.getClient();
            List<RepositoryDTO> allRepository = new ArrayList<>();
            Long page = 1L;
            Long pageSize = 10L;
            while (true) {
                RepositoryUtil.getRepository(client, page, pageSize).forEach(allRepository::add);
                if (allRepository.size() % pageSize != 0) {
                    break;
                }
                page++;
            }
            if (args.length == 2) {
                String orderByColumn = args[1];
                if (orderByColumn.equals("id")) {
                    String s = RepositoryUtil.fillRepositoryListOrderById(allRepository);
                    log.info(s);
                    return;
                }
                if (orderByColumn.equals("name")) {
                    String s = RepositoryUtil.fillRepositoryListOrderByName(allRepository);
                    log.info(s);
                    return;
                }
            }
            String s = RepositoryUtil.fillRepositoryListOrderByName(allRepository);
            log.info(s);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String helpMessage() {
        return "[orderByColumn]\n\t\tGet all repository, The supported columns are:\n\n\t\tid\trepository id.\n\t\tname\trepository name. (default)";
    }

    @Override
    public String option() {
        return "a";
    }
}
