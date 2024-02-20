package com.example.util;

import com.aliyun.devops20210625.Client;
import com.aliyun.devops20210625.models.*;
import com.aliyun.tea.TeaException;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class BranchUtil {


    public static GetBranchInfoResponseBody.GetBranchInfoResponseBodyResult getBranch(Client client, Long repositoryId, String branchName) {
        GetBranchInfoRequest request = new GetBranchInfoRequest();
        request.setBranchName(branchName);
        request.setOrganizationId(ClientUtil.ORGANIZATION_ID);

        try {
            GetBranchInfoResponse branchInfo = client.getBranchInfo(String.valueOf(repositoryId), request);
            return branchInfo.getBody().getResult();
        } catch (Exception e) {
        }
        return null;
    }

    public static List<ListRepositoryBranchesResponseBody.ListRepositoryBranchesResponseBodyResult> getBranches(Client client, Long repositoryId) {
        return getBranches(client, repositoryId, 1L);
    }

    public static List<ListRepositoryBranchesResponseBody.ListRepositoryBranchesResponseBodyResult> getBranches(Client client, Long repositoryId, Long page) {
        return getBranches(client, repositoryId, page, 10L);
    }

    public static List<ListRepositoryBranchesResponseBody.ListRepositoryBranchesResponseBodyResult> getBranches(Client client, Long repositoryId, Long page, Long pageSize) {
        ListRepositoryBranchesRequest request = new ListRepositoryBranchesRequest();
        request.setPage(page);
        request.setPageSize(pageSize);
        request.setOrganizationId(ClientUtil.ORGANIZATION_ID);
        ListRepositoryBranchesResponse response = null;
        try {
            response = client.listRepositoryBranches(String.valueOf(repositoryId), request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return response.getBody().getResult();
    }

    // 是否已经合并到master
    public static boolean isAlreadyMergedMaster(Client client, Long repositoryId, String sourceBranch, String targetBranch, String title) {

        CreateMergeRequestRequest request = mmm(repositoryId, sourceBranch, targetBranch, title);
        CreateMergeRequestResponse response;
        try {
            response = client.createMergeRequest(String.valueOf(repositoryId), request);
        } catch (Exception e) {
            if (((TeaException) e).message.contains("源分支相对目标分支没有改动")) {
                return true;
            }
            if (((TeaException) e).message.contains("无法完成此操作！存在进行中的合并请求")) {
                return false;
            }
            throw new RuntimeException(e);
        }
        Long localId = response.getBody().getResult().getLocalId();
        closeMergeRequest(client, repositoryId, localId);
        return false;
    }

    public static CreateMergeRequestRequest mmm(Long repositoryId, String sourceBranch, String targetBranch, String title) {
        CreateMergeRequestRequest createMergeRequestRequest = new CreateMergeRequestRequest();
        createMergeRequestRequest.setOrganizationId(ClientUtil.ORGANIZATION_ID);
        createMergeRequestRequest.setSourceProjectId(repositoryId);
        createMergeRequestRequest.setSourceBranch(sourceBranch);
        createMergeRequestRequest.setReviewerIds(new ArrayList<>());
        createMergeRequestRequest.setTargetProjectId(repositoryId);
        createMergeRequestRequest.setTargetBranch(targetBranch);
        createMergeRequestRequest.setTitle(title);
        createMergeRequestRequest.setCreateFrom("WEB");

        return createMergeRequestRequest;
    }

    private static void closeMergeRequest(Client client, Long repositoryId, Long mergeRequestId) {
        CloseMergeRequestRequest closeMergeRequestRequest = new CloseMergeRequestRequest();
        closeMergeRequestRequest.setOrganizationId(ClientUtil.ORGANIZATION_ID);
        CloseMergeRequestResponse response = null;
        try {
            response = client.closeMergeRequest(String.valueOf(repositoryId), String.valueOf(mergeRequestId), closeMergeRequestRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteBranch(Client client, Long repositoryId, String branchName) {
        DeleteBranchRequest deleteBranchRequest = new DeleteBranchRequest();
        deleteBranchRequest.setOrganizationId(ClientUtil.ORGANIZATION_ID);
        deleteBranchRequest.setBranchName(branchName);
        DeleteBranchResponse response = null;
        try {
            response = client.deleteBranch(String.valueOf(repositoryId), deleteBranchRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isHalfYearAgoBranch(GetBranchInfoResponseBody.GetBranchInfoResponseBodyResult branch) {
        // 2024-02-05T16:53:19+08:00
        String createdAt = branch.getCommit().getCreatedAt();
        String[] split = createdAt.split("T");
        String date = split[0];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            long time = sdf.parse(date).getTime();
            long currentTime = System.currentTimeMillis();
            long halfYear = 1000L * 60 * 60 * 24 * 365 / 2;
            return currentTime - time > halfYear;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean is2MonthYearAgoBranch(GetBranchInfoResponseBody.GetBranchInfoResponseBodyResult branch) {
        // 2024-02-05T16:53:19+08:00
        String createdAt = branch.getCommit().getCreatedAt();
        String[] split = createdAt.split("T");
        String date = split[0];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            long time = sdf.parse(date).getTime();
            long currentTime = System.currentTimeMillis();
            long halfYear = 1000L * 60 * 60 * 24 * 60;
            return currentTime - time > halfYear;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
