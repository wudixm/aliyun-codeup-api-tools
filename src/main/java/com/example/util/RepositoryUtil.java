package com.example.util;

import com.aliyun.devops20210625.Client;
import com.aliyun.devops20210625.models.ListRepositoriesRequest;
import com.aliyun.devops20210625.models.ListRepositoriesResponse;
import com.aliyun.devops20210625.models.ListRepositoriesResponseBody;
import com.example.dto.RepositoryDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
public class RepositoryUtil {

    public static List<RepositoryDTO> getRepository(Client client) {
        return getRepository(client, 1L);
    }

    public static List<RepositoryDTO> getRepository(Client client, Long page) {
        return getRepository(client, page, 10L);
    }

    public static List<RepositoryDTO> getRepository(Client client, Long page, Long pageSize) {

        ListRepositoriesRequest getRepositoryCommitRequest = new ListRepositoriesRequest();
        getRepositoryCommitRequest.setOrganizationId(ClientUtil.ORGANIZATION_ID);
        getRepositoryCommitRequest.setPage(page);
        getRepositoryCommitRequest.setPerPage(pageSize);

        ListRepositoriesResponse response = null;
        try {
            response = client.listRepositories(getRepositoryCommitRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        List<ListRepositoriesResponseBody.ListRepositoriesResponseBodyResult> result = response.getBody().getResult();
        List<RepositoryDTO> projectInfoList = new ArrayList<>();
        for (ListRepositoriesResponseBody.ListRepositoriesResponseBodyResult r : result) {
            RepositoryDTO repositoryDTO = new RepositoryDTO(r.getId(), r.getName(), r.getWebUrl());
            projectInfoList.add(repositoryDTO);
        }
        return projectInfoList;
    }

    public static String fillRepository(RepositoryDTO dto) {
        return dto.getId() + "," + dto.getName() + "," + dto.getWebUrl() + "\n";
    }

    public static String fillRepositoryListOrderById(List<RepositoryDTO> allRepository) {
        StringBuilder sb = new StringBuilder();
        sb.append("Repository: \n");
        sb.append(String.format("%-10s %1s %-35s %1s %-100s\n", "id", "|", "name", "|", "webUrl"));
        sb.append(String.format("%-10s %1s %-35s %1s %-100s\n", "----------", "|", "-----------------------------------", "|", "----------------------------------------------------------------------------------------------------"));
        TreeMap<Integer, RepositoryDTO> repositoryMap = new TreeMap<>();
        for (RepositoryDTO r : allRepository) {
            repositoryMap.put(r.getId().intValue(), r);
        }
        for (Map.Entry<Integer, RepositoryDTO> entry : repositoryMap.entrySet()) {
            RepositoryDTO r = entry.getValue();
            sb.append(String.format("%-10s %1s %-35s %1s %-100s\n", r.getId(), "|", r.getName(), "|", r.getWebUrl()));
        }
        return sb.toString();
    }

    public static String fillRepositoryListOrderByName(List<RepositoryDTO> allRepository) {
        StringBuilder sb = new StringBuilder();
        sb.append("Repository: \n");
        sb.append(String.format("%-35s %1s %-10s %1s %-100s\n", "name", "|", "id", "|", "webUrl"));
        sb.append(String.format("%-35s %1s %-10s %1s %-100s\n", "-----------------------------------", "|", "----------", "|", "----------------------------------------------------------------------------------------------------"));
        TreeMap<String, RepositoryDTO> repositoryMap = new TreeMap<>();
        for (RepositoryDTO r : allRepository) {
            repositoryMap.put(r.getName(), r);
        }
        for (Map.Entry<String, RepositoryDTO> entry : repositoryMap.entrySet()) {
            RepositoryDTO r = entry.getValue();
            sb.append(String.format("%-35s %1s %-10s %1s %-100s\n", r.getName(), "|", r.getId(), "|", r.getWebUrl()));
        }
        return sb.toString();
    }


}